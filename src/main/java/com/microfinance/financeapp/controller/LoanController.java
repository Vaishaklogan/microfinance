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

    // ✅ LIST LOANS
    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "loans";
    }

    // ✅ SHOW CREATE FORM
    @GetMapping("/new")
    public String showLoanForm(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", memberRepository.findAll());
        return "add-loan"; // 🔥 MUST MATCH FILE NAME
    }

    // ✅ SAVE LOAN (THIS WAS THE ISSUE)
    @PostMapping
    public String saveLoan(@ModelAttribute Loan loan) {

        if (loan.getMember() == null || loan.getMember().getId() == null) {
            throw new IllegalArgumentException("Member must be selected");
        }

        Member member = memberRepository
                .findById(loan.getMember().getId())
                .orElseThrow();

        loan.setMember(member);

        loanCalculationService.initializeLoan(
                loan,
                member.getGroup().getStartDate());

        loanRepository.save(loan);

        return "redirect:/loans";
    }
}