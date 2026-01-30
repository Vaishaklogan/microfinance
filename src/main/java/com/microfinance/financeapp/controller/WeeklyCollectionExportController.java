// package com.microfinance.financeapp.controller;

// import com.microfinance.financeapp.entity.Loan;
// import com.microfinance.financeapp.repository.LoanRepository;
// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;

// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.time.LocalDate;
// import java.util.List;

// @Controller
// public class WeeklyCollectionExportController {

// private final LoanRepository loanRepository;

// public WeeklyCollectionExportController(LoanRepository loanRepository) {
// this.loanRepository = loanRepository;
// }

// @GetMapping("/weekly-collection/excel")
// public void export(HttpServletResponse response) throws IOException {

// LocalDate today = LocalDate.now();

// List<Loan> loans = loanRepository
// .findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
// "ACTIVE", today, today);

// Workbook workbook = new XSSFWorkbook();
// Sheet sheet = workbook.createSheet("Weekly Collection");

// Row header = sheet.createRow(0);
// header.createCell(0).setCellValue("Group");
// header.createCell(1).setCellValue("Member");
// header.createCell(2).setCellValue("Amount");

// int rowNum = 1;
// for (Loan l : loans) {
// Row row = sheet.createRow(rowNum++);
// row.createCell(0).setCellValue(l.getMember().getGroup().getGroupName());
// row.createCell(1).setCellValue(l.getMember().getName());
// row.createCell(2).setCellValue(l.getWeeklyInstallment());
// }

// response.setContentType(
// "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
// response.setHeader(
// "Content-Disposition",
// "attachment; filename=weekly_collection.xlsx");

// workbook.write(response.getOutputStream());
// workbook.close();
// }
// }
