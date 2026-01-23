package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Group;
import com.microfinance.financeapp.repository.GroupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupRepository groupRepository;

    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // Show all groups
    @GetMapping
    public String listGroups(Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        return "groups";
    }

    // Show add group form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("group", new Group());
        return "add-group";
    }

    // Save group
    @PostMapping
    public String saveGroup(@ModelAttribute Group group) {
        groupRepository.save(group);
        return "redirect:/groups";
    }
}
