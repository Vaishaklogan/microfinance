package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.repository.LoanRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/loans")
public class LoanExportController {

    private final LoanRepository loanRepository;

    public LoanExportController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @GetMapping("/export")
    public void exportLoansToExcel(HttpServletResponse response) throws IOException {
        // Fetch all loans
        List<Loan> loans = loanRepository.findAll();

        // Create Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Loans");

        // Create header row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] headers = { "Loan ID", "Member Name", "Member Code", "Loan Amount", "Interest Amount",
                "Weekly Installment", "Principal Balance", "Interest Balance", "Status", "Start Date", "End Date" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        int rowNum = 1;
        for (Loan loan : loans) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(loan.getId());
            row.createCell(1).setCellValue(loan.getMember() != null ? loan.getMember().getName() : "");
            row.createCell(2).setCellValue(loan.getMember() != null ? loan.getMember().getMemberCode() : "");
            row.createCell(3).setCellValue(loan.getLoanAmount());
            row.createCell(4).setCellValue(loan.getInterestAmount());
            row.createCell(5).setCellValue(loan.getWeeklyInstallment());
            row.createCell(6).setCellValue(loan.getPrincipalBalance());
            row.createCell(7).setCellValue(loan.getInterestBalance());
            row.createCell(8).setCellValue(loan.getStatus());
            row.createCell(9).setCellValue(loan.getStartDate() != null ? loan.getStartDate().toString() : "");
            row.createCell(10).setCellValue(loan.getEndDate() != null ? loan.getEndDate().toString() : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=loans_" + LocalDate.now() + ".xlsx");

        // Write workbook to response
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
