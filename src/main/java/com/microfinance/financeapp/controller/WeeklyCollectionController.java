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

    public WeeklyCollectionController(LoanRepository loanRepository,
            PaymentRepository paymentRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
    }

    // PAGE
    @GetMapping
    public String page(Model model) {
        model.addAttribute("loans", loanRepository.findByStatus("ACTIVE"));
        return "weekly-collection";
    }

    // PAY BUTTON — THIS WAS FAILING
    @PostMapping("/pay")
    public String pay(
            @RequestParam("loanId") Long loanId,
            @RequestParam("paidAmount") double paidAmount,
            @RequestParam("paymentDate") LocalDate paymentDate) {

        Loan loan = loanRepository.findById(loanId).orElseThrow();

        // split logic
        double principalPart = loan.getWeeklyPrincipal();
        double interestPart = loan.getWeeklyInterest();

        // update balances
        loan.setPrincipalBalance(loan.getPrincipalBalance() - principalPart);
        loan.setInterestBalance(loan.getInterestBalance() - interestPart);

        loanRepository.save(loan);

        // save payment
        Payment payment = new Payment();
        payment.setLoan(loan);
        payment.setPaidAmount(paidAmount);
        payment.setPrincipalPaid(principalPart);
        payment.setInterestPaid(interestPart);
        payment.setPaymentDate(paymentDate);

        paymentRepository.save(payment);

        return "redirect:/weekly-collection";
    }
}
