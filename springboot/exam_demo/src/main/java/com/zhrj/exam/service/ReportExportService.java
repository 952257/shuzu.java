package com.zhrj.exam.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhrj.exam.entity.SyncReport;
import com.zhrj.exam.mapper.SyncReportMapper;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class ReportExportService {

    private final SyncReportMapper reportMapper;

    public ReportExportService(SyncReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    public byte[] export(LocalDate date) {
        SyncReport report = reportMapper.selectOne(Wrappers.<SyncReport>lambdaQuery()
                .eq(SyncReport::getReportDate, date)
                .last("limit 1"));
        int newCount = report == null || report.getNewCount() == null ? 0 : report.getNewCount();
        int deletedCount = report == null || report.getDeletedCount() == null ? 0 : report.getDeletedCount();

        XWPFDocument document = new XWPFDocument();
        addTitle(document, date.getYear() + "年 " + pad(date.getMonthValue()) + "月 " + pad(date.getDayOfMonth()) + "日 台账统计报告");
        addLine(document, "新增账户：_" + newCount + "__个");
        addLine(document, "已删除账户：_" + deletedCount + "__个");
        addLine(document, "");
        addLine(document, "某系统");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("导出 Word 失败", ex);
        }
    }

    private void addTitle(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontSize(16);
        run.setFontFamily("宋体");
        run.setText(text);
    }

    private void addLine(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        run.setFontFamily("宋体");
        run.setText(text);
    }

    private String pad(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }
}
