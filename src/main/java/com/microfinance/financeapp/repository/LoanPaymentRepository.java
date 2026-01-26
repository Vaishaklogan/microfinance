package com.microfinance.financeapp.repository;

import com.microfinance.financeapp.entity.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {

    List<LoanPayment> findByPaymentDateBetween(LocalDate start, LocalDate end);

    List<LoanPayment> findByLoanId(Long loanId);
}
