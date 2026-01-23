package com.microfinance.financeapp.service;

import com.microfinance.financeapp.repository.WeeklyCollectionRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExportService {

    private final WeeklyCollectionRepository repo;

    public ExportService(WeeklyCollectionRepository repo) {
        this.repo = repo;
    }

    /* ===================== EXCEL EXPORT ===================== */

    public byte[] exportWeeklyExcel(LocalDate date) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Weekly Report");

        // Header row
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        Cell h1 = header.createCell(0);
        h1.setCellValue("Group");

        Cell h2 = header.createCell(1);
        h2.setCellValue("Amount Collected");

        List<Object[]> data = repo.getGroupWiseCollection(date);

        int rowNum = 1;
        for (Object[] rowData : data) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue((String) rowData[0]);
            row.createCell(1).setCellValue((Double) rowData[1]);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    /* ===================== PDF EXPORT ===================== */

    public byte[] exportWeeklyPdf(LocalDate date) throws Exception {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        document.add(new Paragraph("Weekly Collection Report"));
        document.add(new Paragraph("Date: " + date));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.addCell("Group");
        table.addCell("Amount Collected");

        List<Object[]> data = repo.getGroupWiseCollection(date);
        for (Object[] row : data) {
            table.addCell((String) row[0]);
            table.addCell(String.valueOf(row[1]));
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}
