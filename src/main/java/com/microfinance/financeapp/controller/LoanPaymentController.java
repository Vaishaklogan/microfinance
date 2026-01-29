package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.repository.LoanPaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoanPaymentController {

    private final LoanPaymentRepository repo;

    public LoanPaymentController(LoanPaymentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/collection-history")
    public String history(Model model) {
        model.addAttribute("payments", repo.findAll());
        return "collection-history";
    }
}