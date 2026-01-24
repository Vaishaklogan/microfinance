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

    @GetMapping
    public String membersPage(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("member", new Member());
        model.addAttribute("groups", groupRepository.findAll());
        return "members";
    }

    @PostMapping("/save")
    public String saveMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/members";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return "redirect:/members";
    }
}
