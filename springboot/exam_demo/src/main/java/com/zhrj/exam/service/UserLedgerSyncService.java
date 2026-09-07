package com.zhrj.exam.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhrj.exam.client.BladeAuthClient;
import com.zhrj.exam.client.BladeUserClient;
import com.zhrj.exam.config.MinioProperties;
import com.zhrj.exam.dto.UserLedgerExcel;
import com.zhrj.exam.entity.LocalUserLedger;
import com.zhrj.exam.entity.SyncReport;
import com.zhrj.exam.mapper.LocalUserLedgerMapper;
import com.zhrj.exam.mapper.SyncReportMapper;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserLedgerSyncService {

    private final BladeAuthClient bladeAuthClient;
    private final BladeUserClient bladeUserClient;
    private final LocalUserLedgerMapper ledgerMapper;
    private final SyncReportMapper reportMapper;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public UserLedgerSyncService(BladeAuthClient bladeAuthClient,
                                 BladeUserClient bladeUserClient,
                                 LocalUserLedgerMapper ledgerMapper,
                                 SyncReportMapper reportMapper,
                                 MinioClient minioClient,
                                 MinioProperties minioProperties) {
        this.bladeAuthClient = bladeAuthClient;
        this.bladeUserClient = bladeUserClient;
        this.ledgerMapper = ledgerMapper;
        this.reportMapper = reportMapper;
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Transactional(rollbackFor = Exception.class)
    public SyncReport syncOnce() throws Exception {
        String accessToken = bladeAuthClient.fetchAccessToken();
        List<JsonNode> remoteUsers = bladeUserClient.fetchAllUsers(accessToken);

        int newCount = 0;
        int deletedCount = 0;
        Set<Long> remoteIds = new HashSet<Long>();
        LocalDateTime now = LocalDateTime.now();

        for (JsonNode node : remoteUsers) {
            LocalUserLedger incoming = toLedger(node, now);
            remoteIds.add(incoming.getId());
            LocalUserLedger exists = ledgerMapper.selectById(incoming.getId());
            if (exists == null) {
                ledgerMapper.insert(incoming);
                if (incoming.getIsDeleted() != null && incoming.getIsDeleted() == 1) {
                    deletedCount++;
                } else {
                    newCount++;
                }
            } else {
                boolean becameDeleted = (exists.getIsDeleted() == null || exists.getIsDeleted() == 0)
                        && incoming.getIsDeleted() != null && incoming.getIsDeleted() == 1;
                if (becameDeleted) {
                    deletedCount++;
                }
                ledgerMapper.updateById(incoming);
            }
        }

        List<LocalUserLedger> locals = ledgerMapper.selectList(Wrappers.<LocalUserLedger>lambdaQuery());
        for (LocalUserLedger local : locals) {
            if (!remoteIds.contains(local.getId()) && (local.getIsDeleted() == null || local.getIsDeleted() == 0)) {
                local.setIsDeleted(1);
                local.setLastSyncTime(now);
                ledgerMapper.updateById(local);
                deletedCount++;
            }
        }

        String excelObject = uploadExcel(now);
        int totalCount = ledgerMapper.selectCount(Wrappers.<LocalUserLedger>lambdaQuery()).intValue();
        if (deletedCount == 0) {
            deletedCount = ledgerMapper.selectCount(Wrappers.<LocalUserLedger>lambdaQuery()
                    .eq(LocalUserLedger::getIsDeleted, 1)).intValue();
        }
        SyncReport report = upsertReport(LocalDate.now(), newCount, deletedCount, totalCount, excelObject, now);
        log.info("ledger sync finished, new={}, deleted={}, excel={}", newCount, deletedCount, excelObject);
        return report;
    }

    private SyncReport upsertReport(LocalDate date, int newCount, int deletedCount, int totalCount,
                                    String excelObject, LocalDateTime now) {
        SyncReport report = reportMapper.selectOne(Wrappers.<SyncReport>lambdaQuery()
                .eq(SyncReport::getReportDate, date)
                .last("limit 1"));
        if (report == null) {
            report = new SyncReport();
            report.setReportDate(date);
            report.setCreatedAt(now);
        }
        report.setNewCount(newCount);
        report.setDeletedCount(deletedCount);
        report.setTotalCount(totalCount);
        report.setExcelObject(excelObject);
        if (report.getId() == null) {
            reportMapper.insert(report);
        } else {
            reportMapper.updateById(report);
        }
        return report;
    }

    private String uploadExcel(LocalDateTime now) throws Exception {
        List<LocalUserLedger> all = ledgerMapper.selectList(Wrappers.<LocalUserLedger>lambdaQuery()
                .orderByAsc(LocalUserLedger::getId));
        List<UserLedgerExcel> rows = new ArrayList<UserLedgerExcel>();
        for (LocalUserLedger item : all) {
            UserLedgerExcel row = new UserLedgerExcel();
            row.setId(item.getId());
            row.setTenantId(item.getTenantId());
            row.setAccount(item.getAccount());
            row.setName(item.getName());
            row.setRealName(item.getRealName());
            row.setEmail(item.getEmail());
            row.setPhone(item.getPhone());
            row.setIsDeleted(item.getIsDeleted());
            rows.add(row);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EasyExcel.write(outputStream, UserLedgerExcel.class).sheet("用户台账").doWrite(rows);
        byte[] bytes = outputStream.toByteArray();
        String objectName = "ledger/user-ledger-" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".xlsx";
        ensureBucket();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(objectName)
                .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build());
        return objectName;
    }

    private LocalUserLedger toLedger(JsonNode node, LocalDateTime now) {
        LocalUserLedger ledger = new LocalUserLedger();
        ledger.setId(node.path("id").asLong());
        ledger.setTenantId(text(node, "tenantId"));
        ledger.setAccount(text(node, "account"));
        ledger.setName(text(node, "name"));
        ledger.setRealName(text(node, "realName"));
        ledger.setEmail(text(node, "email"));
        ledger.setPhone(text(node, "phone"));
        ledger.setSex(node.path("sex").isMissingNode() ? null : node.path("sex").asInt());
        ledger.setStatus(node.path("status").isMissingNode() ? null : node.path("status").asInt());
        JsonNode deleted = node.get("isDeleted");
        if (deleted == null || deleted.isMissingNode() || deleted.isNull()) {
            deleted = node.get("is_deleted");
        }
        ledger.setIsDeleted(deleted == null || deleted.isMissingNode() || deleted.isNull() ? 0 : deleted.asInt());
        ledger.setLastSyncTime(now);
        return ledger;
    }

    private void ensureBucket() throws Exception {
        String bucket = minioProperties.getBucket();
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (value.isMissingNode() || value.isNull()) {
            return null;
        }
        return value.asText();
    }
}
