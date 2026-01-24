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

    @GetMapping
    public String groups(Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        return "groups";
    }

    @GetMapping("/new")
    public String newGroup(Model model) {
        model.addAttribute("group", new Group());
        return "add-group";
    }

    @PostMapping
    @SuppressWarnings("null")
    public String saveGroup(@ModelAttribute Group group) {
        groupRepository.save(group);
        return "redirect:/groups";
    }

    @PostMapping("/save")
    @SuppressWarnings("null")
    public String saveGroupLegacy(@ModelAttribute Group group) {
        groupRepository.save(group);
        return "redirect:/groups";
    }

    @GetMapping("/delete/{id}")
    @SuppressWarnings("null")
    public String deleteGroup(@PathVariable Long id) {
        groupRepository.deleteById(Long.valueOf(id));
        return "redirect:/groups";
    }
}
