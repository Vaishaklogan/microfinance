package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Member;
import com.microfinance.financeapp.repository.GroupRepository;
import com.microfinance.financeapp.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    public MemberController(MemberRepository memberRepository,
            GroupRepository groupRepository) {
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
    }

    // Show all members
    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "members";
    }

    // Show add member form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("groups", groupRepository.findAll());
        return "add-member";
    }

    // Save member
    @PostMapping
    public String saveMember(@ModelAttribute Member member) {
        member.setStatus("ACTIVE");
        memberRepository.save(member);
        return "redirect:/members";
    }
}
