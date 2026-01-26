package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.repository.LoanRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final LoanRepository loanRepository;

    public DashboardController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/weekly-collection")
    public String weeklyCollection(Model model) {

        LocalDate today = LocalDate.now();

        model.addAttribute(
                "loans",
                loanRepository
                        .findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                "ACTIVE", today, today)
        );

        return "weekly-collection";
    }
}
