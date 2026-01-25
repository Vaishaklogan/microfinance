package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(String status);

    List<Loan> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String status, LocalDate date1, LocalDate date2);
}