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

        try {
            LocalDate collectionDate = LocalDate.parse(date);
            List<Loan> loans = collectionService.getLoansForCollection(collectionDate);

            if (loans != null && !loans.isEmpty()) {
                loans.removeIf(l -> l.getMember() == null ||
                        l.getMember().getGroup() == null ||
                        l.getMember().getGroup().getId().longValue() != groupId.longValue());
            }

            model.addAttribute("loans", loans);
            model.addAttribute("collectionDate", collectionDate);
            model.addAttribute("groupId", groupId);
            model.addAttribute("groups", groupRepository.findAll());

            return "weekly-collection";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading loans: " + e.getMessage());
            model.addAttribute("groups", groupRepository.findAll());
            return "weekly-collection";
        }
    }

    @PostMapping("/pay")
    public String pay(@RequestParam Long loanId,
            @RequestParam double amount,
            @RequestParam String date,
            @RequestParam Long groupId) {

        try {
            Loan loan = collectionService.getLoansForCollection(
                    LocalDate.parse(date)).stream()
                    .filter(l -> l.getId().longValue() == loanId.longValue())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

            collectionService.applyPayment(loan, amount, LocalDate.parse(date));

            // Redirect back to search results
            return "redirect:/collections/search?groupId=" + groupId + "&date=" + date;
        } catch (Exception e) {
            // On error, redirect back to collections page
            return "redirect:/collections";
        }
    }
}
