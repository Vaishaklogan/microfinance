package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.repository.LoanPaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Controller
public class WeeklyCollectionController {

    private final LoanPaymentRepository loanPaymentRepository;

    public WeeklyCollectionController(LoanPaymentRepository loanPaymentRepository) {
        this.loanPaymentRepository = loanPaymentRepository;
    }

    @GetMapping("/weekly-collection")
    public String viewWeeklyCollection(Model model) {

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        model.addAttribute("collections",
            loanPaymentRepository.findByPaymentDateBetween(startOfWeek, endOfWeek));

        return "weekly-collection";
    }
}
