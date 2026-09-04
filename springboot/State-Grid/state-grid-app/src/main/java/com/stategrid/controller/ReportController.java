package com.stategrid.controller;

import com.stategrid.common.ApiResult;
import com.stategrid.entity.SyncReport;
import com.stategrid.service.ReportExportService;
import com.stategrid.service.UserLedgerSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReportController {

    private final ReportExportService reportExportService;
    private final UserLedgerSyncService userLedgerSyncService;

    @GetMapping("/report/export")
    public ResponseEntity<byte[]> export(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        byte[] bytes = reportExportService.exportWord(date);
        String filename = URLEncoder.encode("台账统计报告-" + date + ".docx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
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
