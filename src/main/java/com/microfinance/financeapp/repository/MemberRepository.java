package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByGroupId(Long groupId);
}
