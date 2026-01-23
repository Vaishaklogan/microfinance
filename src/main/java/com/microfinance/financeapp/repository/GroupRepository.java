package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    // For soft delete logic
    List<Group> findByStatus(String status);
}
