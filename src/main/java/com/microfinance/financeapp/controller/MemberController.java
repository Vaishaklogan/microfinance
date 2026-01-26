package com.microfinance.financeapp.controller;

import com.microfinance.financeapp.entity.Group;
import com.microfinance.financeapp.entity.Member;
import com.microfinance.financeapp.repository.GroupRepository;
import com.microfinance.financeapp.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepo;
    private final GroupRepository groupRepo;

    public MemberController(MemberRepository memberRepo, GroupRepository groupRepo) {
        this.memberRepo = memberRepo;
        this.groupRepo = groupRepo;
    }

    // LIST MEMBERS
    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberRepo.findByStatus("ACTIVE"));
        return "members";
    }

    // SHOW ADD FORM
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("groups", groupRepo.findAll());
        return "add-member";
    }

    // SAVE MEMBER ✅ FIXED
    @PostMapping
    public String save(
            @RequestParam String name,
            @RequestParam String aadhaar,
            @RequestParam String address,
            @RequestParam Long groupId) {

        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Member member = new Member();
        member.setName(name);
        member.setPhone(aadhaar); // mapped correctly
        member.setAddress(address);
        member.setGroup(group);
        member.setStatus("ACTIVE");

        // AUTO MEMBER CODE
        member.setMemberCode("M-" + UUID.randomUUID().toString().substring(0, 8));

        memberRepo.save(member);

        return "redirect:/members";
    }

    // SOFT DELETE
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Member m = memberRepo.findById(id).orElseThrow();
        m.setStatus("INACTIVE");
        memberRepo.save(m);
        return "redirect:/members";
    }
}