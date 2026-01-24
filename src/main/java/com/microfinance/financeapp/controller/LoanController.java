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
    public String saveLoan(@RequestParam Long memberId,
            @RequestParam double loanAmount,
            @RequestParam double interestAmount,
            @RequestParam int repaymentWeeks) {

        // Fetch the member
        Member member = memberRepository.findById(memberId).orElseThrow();

        // Create and initialize loan
        Loan loan = new Loan();
        loan.setMember(member);
        loan.setLoanAmount(loanAmount);
        loan.setPrincipalAmount(loanAmount);
        loan.setInterestAmount(interestAmount);
        loan.setRepaymentWeeks(repaymentWeeks);
        loan.setStatus("PENDING");

        // Save the loan
        loanRepository.save(loan);

        return "redirect:/loans";
    }

    @GetMapping("/delete/{id}")
    @SuppressWarnings("null")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(Long.valueOf(id));
        return "redirect:/loans";
    }
}
