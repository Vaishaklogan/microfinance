package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.Member;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.MemberRepository;
import com.microfinance.financeapp.service.LoanCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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

    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "loans";
    }

    @GetMapping("/new")
    public String showLoanForm(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "add-loan";
    }

    @PostMapping("/save")
    public String saveLoan(
            @RequestParam Long memberId,
            @RequestParam double principalAmount,
            @RequestParam double interestAmount,
            @RequestParam int repaymentWeeks) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        Loan loan = new Loan();
        loan.setMember(member);
        loan.setPrincipalAmount(principalAmount);
        loan.setInterestAmount(interestAmount);
        loan.setRepaymentWeeks(repaymentWeeks);

        loanCalculationService.initializeLoan(
                loan,
                member.getGroup().getStartDate());

        loanRepository.save(loan);
        return "redirect:/loans";
    }
}
