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

    public LoanController(LoanRepository loanRepository,
            MemberRepository memberRepository,
            LoanCalculationService loanCalculationService) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.loanCalculationService = loanCalculationService;
    }

    // List all loans
    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "loans";
    }

    // Show create loan form
    @GetMapping("/new")
    public String showLoanForm(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", memberRepository.findAll());
        return "loans-create";
    }

    // Save loan with auto-calculation
    @PostMapping
    public String saveLoan(@ModelAttribute Loan loan) {

        Member member = memberRepository.findById(
                loan.getMember().getId()).orElseThrow();

        loan.setMember(member);

        // Use group start date
        loanCalculationService.initializeLoan(
                loan,
                member.getGroup().getStartDate());

        loanRepository.save(loan);
        return "redirect:/loans";
    }

    @PostMapping("/close/{id}")
    public String closeLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow();
        loan.setStatus("CLOSED");
        loanRepository.save(loan);
        return "redirect:/loans";
    }

}