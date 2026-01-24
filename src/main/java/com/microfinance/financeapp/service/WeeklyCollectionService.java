package com.microfinance.financeapp.service;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.WeeklyCollection;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.WeeklyCollectionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeeklyCollectionService {

    private final LoanRepository loanRepository;
    private final WeeklyCollectionRepository collectionRepository;

    public WeeklyCollectionService(LoanRepository loanRepository,
            WeeklyCollectionRepository collectionRepository) {
        this.loanRepository = loanRepository;
        this.collectionRepository = collectionRepository;
    }

    // Get loans eligible for collection on a date
    public List<Loan> getLoansForCollection(LocalDate date) {
        return loanRepository
                .findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        "ACTIVE", date, date);
    }

    // Apply payment with proper principal and interest split
    public void applyPayment(Loan loan, double amount, LocalDate date) {
        // Calculate the ratio of principal to interest based on weekly installment
        // split
        double weeklyTotal = loan.getWeeklyInstallment();
        double weeklyPrincipal = loan.getWeeklyPrincipal();

        // Calculate principal portion based on ratio
        double principalRatio = weeklyPrincipal / weeklyTotal;

        // First, calculate principal portion
        double principalToDeduct = round(amount * principalRatio);

        // Interest gets the remaining to ensure amount is fully allocated (e.g., 769.4
        // + 230.6 = 1000)
        double interestToDeduct = round(amount - principalToDeduct);

        // Ensure we don't deduct more than what's remaining
        if (principalToDeduct > loan.getPrincipalBalance()) {
            principalToDeduct = loan.getPrincipalBalance();
        }
        if (interestToDeduct > loan.getInterestBalance()) {
            interestToDeduct = loan.getInterestBalance();
        }

        // Update loan balances
        loan.setInterestBalance(round(loan.getInterestBalance() - interestToDeduct));
        loan.setPrincipalBalance(round(loan.getPrincipalBalance() - principalToDeduct));

        // Mark loan as CLOSED if all balances are paid
        if (loan.getPrincipalBalance() <= 0 && loan.getInterestBalance() <= 0) {
            loan.setStatus("CLOSED");
        }

        // Record the payment
        WeeklyCollection wc = new WeeklyCollection();
        wc.setLoan(loan);
        wc.setCollectionDate(date);
        wc.setAmountPaid(amount);
        wc.setPrincipalPaid(principalToDeduct);
        wc.setInterestPaid(interestToDeduct);

        collectionRepository.save(wc);
        loanRepository.save(loan);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
