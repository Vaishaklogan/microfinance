package com.microfinance.financeapp.service;

import com.microfinance.financeapp.entity.Loan;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanCalculationService {

    public void initializeLoan(Loan loan, LocalDate groupStartDate) {

        // 1. Set start date
        loan.setStartDate(groupStartDate);

        // 2. Calculate end date (repaymentWeeks × 7 days)
        LocalDate endDate = groupStartDate.plusWeeks(loan.getRepaymentWeeks());
        loan.setEndDate(endDate);

        // 3. Total payable
        double totalPayable = loan.getPrincipalAmount() + loan.getInterestAmount();

        // 4. Weekly installment
        double weeklyInstallment = totalPayable / loan.getRepaymentWeeks();
        loan.setWeeklyInstallment(round(weeklyInstallment));

        // 5. Ratios
        double principalRatio = loan.getPrincipalAmount() / totalPayable;
        double interestRatio = loan.getInterestAmount() / totalPayable;

        // 6. Weekly split
        loan.setWeeklyPrincipal(round(weeklyInstallment * principalRatio));
        loan.setWeeklyInterest(round(weeklyInstallment * interestRatio));

        // 7. Initialize balances
        loan.setPrincipalBalance(loan.getPrincipalAmount());
        loan.setInterestBalance(loan.getInterestAmount());

        // 8. Status
        loan.setStatus("ACTIVE");
    }

    // Utility: round to 2 decimals
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
