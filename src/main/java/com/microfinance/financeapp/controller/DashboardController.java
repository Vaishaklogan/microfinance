package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        var totals = dashboardService.calculateTotals();
        model.addAttribute("totals", totals);
        return "dashboard";
    }
}
