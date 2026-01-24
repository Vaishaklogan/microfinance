package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.Member;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;

    public LoanController(LoanRepository loanRepository, MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String loans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "loans";
    }

    @GetMapping("/new")
    public String newLoan(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", memberRepository.findAll());
        return "add-loan";
    }

    @PostMapping("/save")
    @SuppressWarnings("null")
    public String saveLoan(@RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long member_id,
            @RequestParam double loanAmount,
            @RequestParam double interestAmount,
            @RequestParam int repaymentWeeks) {

        // Handle both memberId and member.id parameter formats
        Long actualMemberId = memberId != null ? memberId : member_id;
        if (actualMemberId == null) {
            return "redirect:/loans/new?error=true";
        }

        // Fetch the member
        Member member = memberRepository.findById(actualMemberId).orElseThrow();

        // Create and initialize loan
        Loan loan = new Loan();
        loan.setMember(member);
        loan.setLoanAmount(loanAmount);
        loan.setPrincipalAmount(loanAmount);
        loan.setInterestAmount(interestAmount);
        loan.setRepaymentWeeks(repaymentWeeks);
        loan.setTotalWeeks(repaymentWeeks);
        loan.setStatus("ACTIVE");

        // Calculate weekly amounts
        double weeklyTotal = (loanAmount + interestAmount) / repaymentWeeks;
        double weeklyPrincipal = loanAmount / repaymentWeeks;
        double weeklyInterest = interestAmount / repaymentWeeks;

        loan.setWeeklyInstallment(roundTo2Decimals(weeklyTotal));
        loan.setWeeklyPrincipal(roundTo2Decimals(weeklyPrincipal));
        loan.setWeeklyInterest(roundTo2Decimals(weeklyInterest));

        // Set balances equal to principal and interest amounts
        loan.setPrincipalBalance(loanAmount);
        loan.setInterestBalance(interestAmount);

        // Set dates
        loan.setStartDate(java.time.LocalDate.now());
        loan.setEndDate(java.time.LocalDate.now().plusWeeks(repaymentWeeks));

        // Save the loan
        loanRepository.save(loan);

        return "redirect:/loans";
    }

    private double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @GetMapping("/delete/{id}")
    @SuppressWarnings("null")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(Long.valueOf(id));
        return "redirect:/loans";
    }
}
