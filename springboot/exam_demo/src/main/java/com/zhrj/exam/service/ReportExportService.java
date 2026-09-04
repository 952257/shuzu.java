package com.zhrj.exam.service;

import com.zhrj.exam.entity.SyncReport;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportExportService {

    private final UserLedgerSyncService userLedgerSyncService;

    public byte[] exportWord(LocalDate date) {
        SyncReport report = userLedgerSyncService.findByDate(date);
        try {
            XWPFDocument document = new XWPFDocument();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                title(document, String.format("%d年 %d月 %d日 台账统计报告",
                        date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
                body(document, String.format("新增账户：_%d__个", nvl(report.getNewCount())));
                body(document, String.format("已删除账户：_%d__个", nvl(report.getDeletedCount())));
                body(document, "国家电网数据平台");
                document.write(outputStream);
                return outputStream.toByteArray();
            } finally {
                document.close();
                outputStream.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException("生成 Word 失败: " + e.getMessage(), e);
        }
    }

    private void title(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(16);
        run.setFontFamily("宋体");
    }

    private void body(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(14);
        run.setFontFamily("宋体");
    }

    private int nvl(Integer value) {
        return value == null ? 0 : value;
    }
}
