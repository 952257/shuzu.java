package com.zhrj.exam.controller;

import com.zhrj.exam.common.ApiResult;
import com.zhrj.exam.entity.SyncReport;
import com.zhrj.exam.service.ReportExportService;
import com.zhrj.exam.service.UserLedgerSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportExportService reportExportService;
    private final UserLedgerSyncService userLedgerSyncService;

    @GetMapping("/report/export")
    public ResponseEntity<byte[]> export(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        byte[] bytes = reportExportService.exportWord(date);
        String filename;
        try {
            filename = URLEncoder.encode("台账统计报告-" + date + ".docx", "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            filename = "report.docx";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(bytes);
    }

    @GetMapping("/report/stat")
    public ApiResult<SyncReport> stat(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResult.ok(userLedgerSyncService.findByDate(date));
    }

    @PostMapping("/job/sync")
    public ApiResult<SyncReport> syncNow() {
        return ApiResult.ok(userLedgerSyncService.syncOnce());
    }
}
