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

    private final MemberRepository memberRepo;
    private final GroupRepository groupRepo;

    public MemberController(MemberRepository memberRepo, GroupRepository groupRepo) {
        this.memberRepo = memberRepo;
        this.groupRepo = groupRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberRepo.findByStatus("ACTIVE"));
        return "members";
    }

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("groups", groupRepo.findAll());
        return "add-member";
    }

    @PostMapping
    public String save(Member member) {
        member.setStatus("ACTIVE");
        memberRepo.save(member);
        return "redirect:/members";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Member m = memberRepo.findById(id).orElseThrow();
        m.setStatus("INACTIVE");
        memberRepo.save(m);
        return "redirect:/members";
    }
}