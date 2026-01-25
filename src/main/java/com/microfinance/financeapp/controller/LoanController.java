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

    // ✅ OPEN CREATE FORM
    @GetMapping("/new")
    public String showLoanForm(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", memberRepository.findAll());
        return "loans-create";
    }

    @PostMapping("/save")
    public String saveLoan(@ModelAttribute Loan loan) {

        System.out.println(">>> SAVE LOAN HIT");
        System.out.println("Principal: " + loan.getPrincipalAmount());
        System.out.println("Interest: " + loan.getInterestAmount());
        System.out.println("Weeks: " + loan.getRepaymentWeeks());
        System.out.println("Member ID: " +
                (loan.getMember() != null ? loan.getMember().getId() : "NULL"));

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
