package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.Payment;
import com.microfinance.financeapp.entity.Group;
import com.microfinance.financeapp.repository.GroupRepository;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.PaymentRepository;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/weekly-collection")
public class WeeklyCollectionController {

    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final GroupRepository groupRepository;

    public WeeklyCollectionController(
            LoanRepository loanRepository,
            PaymentRepository paymentRepository,
            GroupRepository groupRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.groupRepository = groupRepository;
    }

    // ONLY ONE GET
    @GetMapping
    public String weeklyCollectionPage(Model model,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long groupId) {

        LocalDate selectedDate = date != null ? date : LocalDate.now();

        // groups (ALL GROUPS option will be represented by null groupId)
        List<Group> groups = groupRepository.findByStatus("ACTIVE");
        groups.sort(Comparator.comparing(Group::getGroupName, Comparator.nullsFirst(String::compareTo)));

        // fetch active loans for the selected date range
        List<Loan> loans = loanRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                "ACTIVE", selectedDate, selectedDate);

        // apply group filter if specified
        if (groupId != null) {
            loans = loans.stream()
                    .filter(l -> l.getMember() != null && l.getMember().getGroup() != null &&
                            Objects.equals(l.getMember().getGroup().getId(), groupId))
                    .collect(Collectors.toList());
        }

        // group loans by group id in the order of groups list
        Map<Long, List<Loan>> loansByGroup = new LinkedHashMap<>();
        for (Group g : groups) {
            List<Loan> grpLoans = loans.stream()
                    .filter(l -> l.getMember() != null && l.getMember().getGroup() != null &&
                            Objects.equals(l.getMember().getGroup().getId(), g.getId()))
                    .collect(Collectors.toList());
            if (!grpLoans.isEmpty()) {
                loansByGroup.put(g.getId(), grpLoans);
            }
        }

        model.addAttribute("groups", groups);
        model.addAttribute("loansByGroup", loansByGroup);
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("selectedDate", selectedDate);

        return "weekly-collection";
    }

    // ONLY ONE POST
    @PostMapping("/pay")
    public String payWeeklyAmount(@RequestParam Long loanId,
            @RequestParam double paidAmount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Loan loan = loanRepository.findById(loanId).orElseThrow();

        // Validation
        if (paidAmount <= 0) {
            return "redirect:/weekly-collection?error=Invalid+amount&groupId=" + (groupId == null ? "" : groupId)
                    + (date == null ? "" : "&date=" + date);
        }

        double remainingPrincipal = loan.getPrincipalBalance();
        double remainingInterest = loan.getInterestBalance();
        double remainingTotal = Math.max(0, remainingPrincipal) + Math.max(0, remainingInterest);

        if (paidAmount > remainingTotal) {
            return "redirect:/weekly-collection?error=Overpayment+not+allowed&groupId="
                    + (groupId == null ? "" : groupId) + (date == null ? "" : "&date=" + date);
        }

        // allocate to interest first, then principal
        double interestPaid = Math.min(remainingInterest, paidAmount);
        double principalPaid = paidAmount - interestPaid;

        // Update balances
        loan.setInterestBalance(Math.max(0, remainingInterest - interestPaid));
        loan.setPrincipalBalance(Math.max(0, remainingPrincipal - principalPaid));

        // close loan if fully paid
        if (loan.getPrincipalBalance() <= 0 && loan.getInterestBalance() <= 0) {
            loan.setStatus("CLOSED");
        }

        // Save payment
        Payment payment = new Payment();
        payment.setLoan(loan);
        payment.setPaidAmount(paidAmount);
        payment.setPrincipalPaid(principalPaid);
        payment.setInterestPaid(interestPaid);
        payment.setPaymentDate(paymentDate);

        paymentRepository.save(payment);
        loanRepository.save(loan);

        StringBuilder redirect = new StringBuilder("redirect:/weekly-collection");
        List<String> params = new ArrayList<>();
        if (groupId != null)
            params.add("groupId=" + groupId);
        if (date != null)
            params.add("date=" + date);
        if (!params.isEmpty())
            redirect.append("?").append(String.join("&", params));

        return redirect.toString();
    }
}
