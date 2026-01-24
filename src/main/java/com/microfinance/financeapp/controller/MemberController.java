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
        var groups = groupRepo.findByStatus("ACTIVE");
        if (groups == null || groups.isEmpty()) {
            // Fallback: show all groups if no active groups
            groups = groupRepo.findAll();
        }
        model.addAttribute("groups", groups);
        return "add-member";
    }

    // SAVE MEMBER
    @PostMapping
    @SuppressWarnings("null")
    public String save(@RequestParam String name,
            @RequestParam String aadhaar,
            @RequestParam String address,
            @RequestParam(required = false) String landmark,
            @RequestParam Long groupId) {

        try {
            Group group = groupRepo.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("Group not found"));

            Member member = new Member();
            member.setName(name);
            member.setAadhaar(aadhaar);
            member.setAddress(address);
            member.setLandmark(landmark);
            member.setGroup(group);
            member.setStatus("ACTIVE");

            memberRepo.save(member);
            return "redirect:/members";
        } catch (Exception e) {
            // Log error and redirect back to form
            return "redirect:/members/new?error=true";
        }
    }

    // SOFT DELETE
    @PostMapping("/delete/{id}")
    @SuppressWarnings("null")
    public String delete(@PathVariable Long id) {
        Member member = memberRepo.findById(Long.valueOf(id)).orElseThrow();
        member.setStatus("INACTIVE");
        memberRepo.save(member);
        return "redirect:/members";
    }

    // Search member by memberCode
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String memberCode, Model model) {
        if (memberCode != null && !memberCode.isEmpty()) {
            var member = memberRepo.findByMemberCode(memberCode);
            if (member.isPresent()) {
                model.addAttribute("searchMember", member.get());
                model.addAttribute("memberCode", memberCode);
                model.addAttribute("found", true);
            } else {
                model.addAttribute("found", false);
                model.addAttribute("memberCode", memberCode);
            }
        }
        return "member-search";
    }
}
