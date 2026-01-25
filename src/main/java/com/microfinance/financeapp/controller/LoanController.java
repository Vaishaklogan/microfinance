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
    public String saveLoan(@ModelAttribute Loan loan, @RequestParam Long memberId) {
        try {
            // Log incoming values for debugging
            System.out.println("Saving loan: memberId=" + memberId + ", loanAmount=" + loan.getLoanAmount()
                    + ", interestAmount=" + loan.getInterestAmount() + ", repaymentWeeks=" + loan.getRepaymentWeeks());

            Member member = memberRepository.findById(memberId).orElseThrow();
            loan.setMember(member);

            // Ensure essential fields are set
            if (loan.getRepaymentWeeks() <= 0) {
                return "redirect:/loans/new?error=true";
            }

            // Calculate derived fields
            double loanAmount = loan.getLoanAmount();
            double interestAmount = loan.getInterestAmount();
            int repaymentWeeks = loan.getRepaymentWeeks();

            loan.setPrincipalAmount(loanAmount);
            loan.setInterestAmount(interestAmount);
            loan.setTotalWeeks(repaymentWeeks);
            loan.setStatus("ACTIVE");

            double weeklyTotal = (loanAmount + interestAmount) / repaymentWeeks;
            double weeklyPrincipal = loanAmount / repaymentWeeks;
            double weeklyInterest = interestAmount / repaymentWeeks;

            loan.setWeeklyInstallment(roundTo2Decimals(weeklyTotal));
            loan.setWeeklyPrincipal(roundTo2Decimals(weeklyPrincipal));
            loan.setWeeklyInterest(roundTo2Decimals(weeklyInterest));

            loan.setPrincipalBalance(loanAmount);
            loan.setInterestBalance(interestAmount);

            loan.setStartDate(java.time.LocalDate.now());
            loan.setEndDate(java.time.LocalDate.now().plusWeeks(repaymentWeeks));

            loanRepository.save(loan);
            return "redirect:/loans";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/loans/new?error=true";
        }
    }

    private double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @GetMapping("/delete/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(Long.valueOf(id));
        return "redirect:/loans";
    }

    @PostMapping("/close/{id}")
    public String closeLoan(@PathVariable Long id) {
        var loanOpt = loanRepository.findById(id);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setStatus("CLOSED");
            loan.setPrincipalBalance(0);
            loan.setInterestBalance(0);
            loanRepository.save(loan);
        }
        return "redirect:/loans";
    }
}