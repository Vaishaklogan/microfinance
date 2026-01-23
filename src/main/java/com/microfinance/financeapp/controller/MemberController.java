package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Group;
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

    // List only ACTIVE members
    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberRepo.findByStatus("ACTIVE"));
        return "members";
    }

    // Show add form (only ACTIVE groups)
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("groups", groupRepo.findByStatus("ACTIVE"));
        return "add-member";
    }

    // SAVE MEMBER (IMPORTANT FIX HERE)
    @PostMapping
    public String save(@RequestParam String name,
            @RequestParam String aadhaar,
            @RequestParam String address,
            @RequestParam(required = false) String landmark,
            @RequestParam Long groupId) {

        Group group = groupRepo.findById(groupId).orElseThrow();

        Member member = new Member();
        member.setName(name);
        member.setAadhaar(aadhaar);
        member.setAddress(address);
        member.setLandmark(landmark);
        member.setGroup(group);
        member.setStatus("ACTIVE");

        memberRepo.save(member);

        return "redirect:/members";
    }

    // SOFT DELETE
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Member member = memberRepo.findById(id).orElseThrow();
        member.setStatus("INACTIVE");
        memberRepo.save(member);
        return "redirect:/members";
    }
}
