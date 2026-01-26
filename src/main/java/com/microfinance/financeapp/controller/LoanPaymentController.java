package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.service.LoanPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class LoanPaymentController {

    private final LoanPaymentService paymentService;

    public LoanPaymentController(LoanPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/{loanId}")
    public String pay(@PathVariable Long loanId) {
        paymentService.makeWeeklyPayment(loanId);
        return "redirect:/loans";
    }
}
