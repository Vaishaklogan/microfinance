package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.repository.GroupRepository;
import com.microfinance.financeapp.service.WeeklyCollectionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/collections")
public class WeeklyCollectionController {

    private final WeeklyCollectionService collectionService;
    private final GroupRepository groupRepository;

    public WeeklyCollectionController(WeeklyCollectionService collectionService,
            GroupRepository groupRepository) {
        this.collectionService = collectionService;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public String showCollectionPage(Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        return "weekly-collection";
    }

    @PostMapping("/search")
    public String searchLoans(@RequestParam Long groupId,
            @RequestParam String date,
            Model model) {

        LocalDate collectionDate = LocalDate.parse(date);
        List<Loan> loans = collectionService.getLoansForCollection(collectionDate);

        loans.removeIf(l -> !l.getMember().getGroup().getId().equals(groupId));

        model.addAttribute("loans", loans);
        model.addAttribute("collectionDate", collectionDate);
        model.addAttribute("groupId", groupId);
        model.addAttribute("groups", groupRepository.findAll());

        return "weekly-collection";
    }

    @PostMapping("/pay")
    public String pay(@RequestParam Long loanId,
            @RequestParam double amount,
            @RequestParam String date,
            @RequestParam Long groupId) {

        Loan loan = collectionService.getLoansForCollection(
                LocalDate.parse(date)).stream()
                .filter(l -> l.getId().equals(loanId))
                .findFirst()
                .orElseThrow();

        collectionService.applyPayment(loan, amount, LocalDate.parse(date));

        return "redirect:/collections?groupId=" + groupId;
    }
}
