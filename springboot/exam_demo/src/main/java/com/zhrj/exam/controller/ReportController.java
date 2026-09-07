package com.zhrj.exam.controller;

import com.zhrj.exam.service.ReportExportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
public class ReportController {

    private final ReportExportService reportExportService;

    public ReportController(ReportExportService reportExportService) {
        this.reportExportService = reportExportService;
    }

    @GetMapping("/report/export")
    public ResponseEntity<byte[]> export(@RequestParam("date")
                                         @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        byte[] bytes = reportExportService.export(date);
        String filename;
        try {
            filename = URLEncoder.encode(date + "-台账统计报告.docx", StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            filename = date + "-report.docx";
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(bytes);
    }
}
