package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/weekly")
    public String weeklyReport() {
        return "weekly-report";
    }

    @PostMapping("/weekly")
    public String weeklyReportResult(@RequestParam String date, Model model) {

        LocalDate d = LocalDate.parse(date);

        model.addAttribute("date", d);
        model.addAttribute("total", reportService.getWeeklyDeposit(d));
        model.addAttribute("groups", reportService.getGroupWiseTotals(d));

        return "weekly-report";
    }

    @GetMapping("/yearly")
    public String yearlyReport(@RequestParam int year, Model model) {
        model.addAttribute("plan", reportService.getYearlySundayPlan(year));
        model.addAttribute("year", year);
        return "yearly-report";
    }
}
