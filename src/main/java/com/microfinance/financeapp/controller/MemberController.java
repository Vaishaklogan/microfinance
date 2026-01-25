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
    public String saveMember(@ModelAttribute Member member, @RequestParam Long groupId) {
        try {
            var group = groupRepository.findById(groupId).orElseThrow();
            member.setGroup(group);
            member.setStatus("ACTIVE");
            memberRepository.save(member);
            return "redirect:/members";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/members/new?error=true";
        }
    }

    // Search member by memberCode and show in members list
    @GetMapping("/search")
    public String searchMember(@RequestParam(required = false) String memberCode, Model model) {
        if (memberCode != null && !memberCode.isEmpty()) {
            var opt = memberRepository.findByMemberCode(memberCode);
            if (opt.isPresent()) {
                model.addAttribute("searchMember", opt.get());
                model.addAttribute("memberCode", memberCode);
                model.addAttribute("found", true);
            } else {
                model.addAttribute("found", false);
                model.addAttribute("memberCode", memberCode);
            }
        }

        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        return "members";
    }
}