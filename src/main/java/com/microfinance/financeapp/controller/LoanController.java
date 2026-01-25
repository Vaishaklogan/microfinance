package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.Member;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.MemberRepository;
import com.microfinance.financeapp.service.LoanCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final LoanCalculationService loanCalculationService;

    public LoanController(
            LoanRepository loanRepository,
            MemberRepository memberRepository,
            LoanCalculationService loanCalculationService) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.loanCalculationService = loanCalculationService;
    }

    // -------------------------
    // LIST LOANS
    // -------------------------
    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "loans";
    }

    // -------------------------
    // SHOW CREATE LOAN FORM
    // -------------------------
    @GetMapping("/new")
    public String showCreateLoanForm(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "add-loan";
    }

    // -------------------------
    // SAVE LOAN (THIS WAS BROKEN BEFORE)
    // -------------------------
    @PostMapping("/save")
    public String saveLoan(
            @RequestParam Long memberId,
            @RequestParam double principalAmount,
            @RequestParam double interestAmount,
            @RequestParam int repaymentWeeks) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Loan loan = new Loan();
        loan.setMember(member);
        loan.setPrincipalAmount(principalAmount);
        loan.setInterestAmount(interestAmount);
        loan.setRepaymentWeeks(repaymentWeeks);

        // AUTO CALCULATION
        loanCalculationService.initializeLoan(
                loan,
                member.getGroup().getStartDate());

        loanRepository.save(loan);

        return "redirect:/loans";
    }

    // -------------------------
    // CLOSE LOAN
    // -------------------------
    @PostMapping("/close/{id}")
    public String closeLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus("CLOSED");
        loanRepository.save(loan);

        return "redirect:/loans";
    }
}
