package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.WeeklyCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyCollectionRepository extends JpaRepository<WeeklyCollection, Long> {
}
