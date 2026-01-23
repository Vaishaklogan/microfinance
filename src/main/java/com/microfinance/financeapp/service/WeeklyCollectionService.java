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

    // Apply payment
    public void applyPayment(Loan loan, double amount, LocalDate date) {

        double interestPaid = Math.min(amount, loan.getInterestBalance());
        double principalPaid = amount - interestPaid;

        loan.setInterestBalance(round(loan.getInterestBalance() - interestPaid));
        loan.setPrincipalBalance(round(loan.getPrincipalBalance() - principalPaid));

        if (loan.getPrincipalBalance() <= 0 && loan.getInterestBalance() <= 0) {
            loan.setStatus("CLOSED");
        }

        WeeklyCollection wc = new WeeklyCollection();
        wc.setLoan(loan);
        wc.setCollectionDate(date);
        wc.setAmountPaid(amount);
        wc.setPrincipalPaid(principalPaid);
        wc.setInterestPaid(interestPaid);

        collectionRepository.save(wc);
        loanRepository.save(loan);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
