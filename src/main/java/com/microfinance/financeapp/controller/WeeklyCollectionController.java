package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.Payment;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.PaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/weekly-collection")
public class WeeklyCollectionController {

    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;

    public WeeklyCollectionController(
            LoanRepository loanRepository,
            PaymentRepository paymentRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
    }

    // ONLY ONE GET
    @GetMapping
    public String weeklyCollectionPage(Model model) {
        model.addAttribute("loans", loanRepository.findByStatus("ACTIVE"));
        return "weekly-collection";
    }

    // ONLY ONE POST
    @PostMapping("/pay")
    public String payWeeklyAmount(
            @RequestParam Long loanId,
            @RequestParam double paidAmount,
            @RequestParam LocalDate paymentDate) {

        Loan loan = loanRepository.findById(loanId).orElseThrow();

        double principalPaid = loan.getWeeklyPrincipal();
        double interestPaid = loan.getWeeklyInterest();

        loan.setPrincipalBalance(loan.getPrincipalBalance() - principalPaid);
        loan.setInterestBalance(loan.getInterestBalance() - interestPaid);

        loanRepository.save(loan);

        Payment payment = new Payment();
        payment.setLoan(loan);
        payment.setPaidAmount(paidAmount);
        payment.setPrincipalPaid(principalPaid);
        payment.setInterestPaid(interestPaid);
        payment.setPaymentDate(paymentDate);

        paymentRepository.save(payment);

        return "redirect:/weekly-collection";
    }
}
