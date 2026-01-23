package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Group;
import com.microfinance.financeapp.repository.GroupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupRepository groupRepo;

    public GroupController(GroupRepository groupRepo) {
        this.groupRepo = groupRepo;
    }

    // List only ACTIVE groups
    @GetMapping
    public String list(Model model) {
        model.addAttribute("groups", groupRepo.findByStatus("ACTIVE"));
        return "groups";
    }

    // Show add group form
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("group", new Group());
        return "add-group";
    }

    // SAVE GROUP (IMPORTANT FIX)
    @PostMapping
    public String save(@RequestParam String groupName,
            @RequestParam String startDate) {

        Group group = new Group();
        group.setGroupName(groupName);
        group.setStartDate(java.time.LocalDate.parse(startDate));
        group.setStatus("ACTIVE");

        groupRepo.save(group);

        return "redirect:/groups";
    }

    // SOFT DELETE GROUP
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Group group = groupRepo.findById(id).orElseThrow();
        group.setStatus("INACTIVE");
        groupRepo.save(group);
        return "redirect:/groups";
    }
}
