package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
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

    @GetMapping("/new")
    public String newLoan(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", memberRepository.findAll());
        return "loan-form";
    }

    @PostMapping("/save")
    @SuppressWarnings("null")
    public String saveLoan(@ModelAttribute Loan loan) {
        loanRepository.save(loan);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    @SuppressWarnings("null")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
}
