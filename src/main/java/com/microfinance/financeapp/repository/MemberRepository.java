package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByStatus(String status);

    Optional<Member> findByMemberCode(String memberCode);

    List<Member> findByNameContainingIgnoreCaseAndStatus(String name, String status);
}
