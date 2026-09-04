package com.zhrj.exam.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhrj.exam.client.BladeAuthClient;
import com.zhrj.exam.client.BladeUserClient;
import com.zhrj.exam.config.MinioProperties;
import com.zhrj.exam.config.RemoteAuthProperties;
import com.zhrj.exam.dto.BladeTokenResponse;
import com.zhrj.exam.dto.BladeUserPageResponse;
import com.zhrj.exam.dto.UserLedgerExcel;
import com.zhrj.exam.entity.LocalUserLedger;
import com.zhrj.exam.entity.SyncReport;
import com.zhrj.exam.mapper.LocalUserLedgerMapper;
import com.zhrj.exam.mapper.SyncReportMapper;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class UserLedgerSyncService {

    private final BladeAuthClient bladeAuthClient;
    private final BladeUserClient bladeUserClient;
    private final RemoteAuthProperties remoteAuthProperties;
    private final LocalUserLedgerMapper localUserLedgerMapper;
    private final SyncReportMapper syncReportMapper;
    private final MinioProperties minioProperties;
    private final MinioClient minioClient;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public UserLedgerSyncService(BladeAuthClient bladeAuthClient,
                                 BladeUserClient bladeUserClient,
                                 RemoteAuthProperties remoteAuthProperties,
                                 LocalUserLedgerMapper localUserLedgerMapper,
                                 SyncReportMapper syncReportMapper,
                                 MinioProperties minioProperties,
                                 @Autowired(required = false) MinioClient minioClient) {
        this.bladeAuthClient = bladeAuthClient;
        this.bladeUserClient = bladeUserClient;
        this.remoteAuthProperties = remoteAuthProperties;
        this.localUserLedgerMapper = localUserLedgerMapper;
        this.syncReportMapper = syncReportMapper;
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    @Transactional
    public SyncReport syncOnce() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("台账同步正在执行，请稍后");
        }
        try {
            return doSync();
        } finally {
            running.set(false);
        }
    }

    private SyncReport doSync() {
        BladeTokenResponse tokenResponse = bladeAuthClient.fetchToken();
        String accessToken = tokenResponse.getData().getAccessToken();
        int newCount = 0;
        int deletedCount = 0;
        long current = 1;
        long size = remoteAuthProperties.getUser().getPageSize();
        LocalDateTime now = LocalDateTime.now();
        Set<Long> remoteIds = new HashSet<Long>();
        while (true) {
            BladeUserPageResponse page = bladeUserClient.fetchUsers(accessToken, current, size);
            List<BladeUserPageResponse.RemoteUser> records = page.getData().getRecords();
            if (records == null || records.isEmpty()) {
                break;
            }
            for (BladeUserPageResponse.RemoteUser remote : records) {
                if (remote.getId() != null) {
                    remoteIds.add(remote.getId());
                }
                int[] delta = upsert(remote, now);
                newCount += delta[0];
                deletedCount += delta[1];
            }
            Long pages = page.getData().getPages();
            if (pages == null || current >= pages) {
                break;
            }
            current++;
        }
        deletedCount += markMissingAsDeleted(remoteIds, now);
        String excelObject = exportExcel(LocalDate.now());
        long total = localUserLedgerMapper.selectCount(new QueryWrapper<LocalUserLedger>());
        return saveReport(LocalDate.now(), newCount, deletedCount, (int) total, excelObject);
    }

    private int[] upsert(BladeUserPageResponse.RemoteUser remote, LocalDateTime now) {
        LocalUserLedger local = localUserLedgerMapper.selectById(remote.getId());
        if (local == null) {
            localUserLedgerMapper.insert(toEntity(remote, now));
            int deleted = Integer.valueOf(1).equals(remote.getIsDeleted()) ? 1 : 0;
            return new int[]{1, deleted};
        }
        int oldDeleted = local.getIsDeleted() == null ? 0 : local.getIsDeleted();
        int newDeleted = remote.getIsDeleted() == null ? 0 : remote.getIsDeleted();
        LocalUserLedger entity = toEntity(remote, now);
        entity.setCreateTime(local.getCreateTime());
        localUserLedgerMapper.updateById(entity);
        return new int[]{0, (oldDeleted == 0 && newDeleted == 1) ? 1 : 0};
    }

    /**
     * Blade 逻辑删除后 user-list 不再返回该用户，本地仍在则记一次删除。
     */
    private int markMissingAsDeleted(Set<Long> remoteIds, LocalDateTime now) {
        int deleted = 0;
        List<LocalUserLedger> locals = localUserLedgerMapper.selectList(new QueryWrapper<LocalUserLedger>());
        for (LocalUserLedger local : locals) {
            if (local.getIsDeleted() != null && local.getIsDeleted() == 1) {
                continue;
            }
            if (!remoteIds.contains(local.getId())) {
                local.setIsDeleted(1);
                local.setUpdateTime(now);
                local.setSyncTime(now);
                localUserLedgerMapper.updateById(local);
                deleted++;
            }
        }
        return deleted;
    }

    private LocalUserLedger toEntity(BladeUserPageResponse.RemoteUser remote, LocalDateTime now) {
        LocalUserLedger entity = new LocalUserLedger();
        entity.setId(remote.getId());
        entity.setTenantId(remote.getTenantId());
        entity.setAccount(remote.getAccount());
        entity.setName(remote.getName());
        entity.setRealName(remote.getRealName());
        entity.setEmail(remote.getEmail());
        entity.setPhone(remote.getPhone());
        entity.setRoleId(remote.getRoleId());
        entity.setDeptId(remote.getDeptId());
        entity.setStatus(remote.getStatus());
        entity.setIsDeleted(remote.getIsDeleted() == null ? 0 : remote.getIsDeleted());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setSyncTime(now);
        return entity;
    }

    private String exportExcel(LocalDate date) {
        List<LocalUserLedger> all = localUserLedgerMapper.selectList(
                new LambdaQueryWrapper<LocalUserLedger>().orderByAsc(LocalUserLedger::getId));
        List<UserLedgerExcel> rows = new ArrayList<UserLedgerExcel>();
        for (LocalUserLedger item : all) {
            UserLedgerExcel excel = new UserLedgerExcel();
            BeanUtils.copyProperties(item, excel);
            rows.add(excel);
        }
        File dir = new File("data/excel");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("无法创建目录 " + dir.getAbsolutePath());
        }
        String fileName = "user-ledger-" + date.format(DateTimeFormatter.ISO_DATE) + ".xlsx";
        File file = new File(dir, fileName);
        EasyExcel.write(file, UserLedgerExcel.class).sheet("用户台账").doWrite(rows);
        log.info("台账 Excel 已写入 {}", file.getAbsolutePath());
        return uploadMinio(file, fileName);
    }

    private String uploadMinio(File file, String fileName) {
        if (minioClient == null || minioProperties == null || !minioProperties.isEnabled()) {
            return file.getAbsolutePath();
        }
        try {
            String object = "user-ledger/" + fileName;
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(object)
                    .filename(file.getAbsolutePath())
                    .build());
            log.info("Excel 已上传 MinIO bucket={} object={}", minioProperties.getBucket(), object);
            return minioProperties.getBucket() + "/" + object;
        } catch (Exception e) {
            log.warn("MinIO 不可用，Excel 仅保存在本地: {}", e.getMessage());
            return file.getAbsolutePath();
        }
    }

    private SyncReport saveReport(LocalDate date, int newCount, int deletedCount, int totalCount, String excelObject) {
        SyncReport report = syncReportMapper.selectOne(
                new LambdaQueryWrapper<SyncReport>().eq(SyncReport::getReportDate, date));
        LocalDateTime now = LocalDateTime.now();
        if (report == null) {
            report = new SyncReport();
            report.setReportDate(date);
            report.setNewCount(newCount);
            report.setDeletedCount(deletedCount);
            report.setTotalCount(totalCount);
            report.setExcelObject(excelObject);
            report.setCreateTime(now);
            report.setUpdateTime(now);
            syncReportMapper.insert(report);
            return report;
        }
        report.setNewCount(report.getNewCount() + newCount);
        report.setDeletedCount(report.getDeletedCount() + deletedCount);
        report.setTotalCount(totalCount);
        report.setExcelObject(excelObject);
        report.setUpdateTime(now);
        syncReportMapper.updateById(report);
        return report;
    }

    public SyncReport findByDate(LocalDate date) {
        SyncReport report = syncReportMapper.selectOne(
                new LambdaQueryWrapper<SyncReport>().eq(SyncReport::getReportDate, date));
        if (report == null) {
            report = new SyncReport();
            report.setReportDate(date);
            report.setNewCount(0);
            report.setDeletedCount(0);
            Long total = localUserLedgerMapper.selectCount(new QueryWrapper<LocalUserLedger>());
            report.setTotalCount(total == null ? 0 : total.intValue());
        }
        return report;
    }
}
