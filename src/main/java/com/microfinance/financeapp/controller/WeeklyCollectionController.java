package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.repository.LoanPaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequestMapping("/weekly-collection")
public class WeeklyCollectionController {

    // PAGE LOAD
    @GetMapping
    public String viewWeeklyCollection(Model model) {
        // load data for UI
        return "weekly-collection";
    }

    @PostMapping("/pay")
    public String payWeeklyCollection(
            @RequestParam("loanId") Long loanId,
            @RequestParam("paidAmount") double paidAmount,
            @RequestParam("paymentDate") LocalDate paymentDate) {
        System.out.println(loanId + " " + paidAmount + " " + paymentDate);
        return "redirect:/weekly-collection";
    }

}
