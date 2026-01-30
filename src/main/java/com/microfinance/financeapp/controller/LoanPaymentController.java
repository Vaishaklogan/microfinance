package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.repository.PaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoanPaymentController {

    private final PaymentRepository paymentRepo;

    public LoanPaymentController(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("/collection-history")
    public String history(Model model) {
        model.addAttribute("collections", paymentRepo.findAll());
        return "collection-history";
    }
}