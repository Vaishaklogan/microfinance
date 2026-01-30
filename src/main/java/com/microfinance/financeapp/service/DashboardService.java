package com.microfinance.financeapp.service;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.repository.LoanRepository;
import com.microfinance.financeapp.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;

    public DashboardService(LoanRepository loanRepository, PaymentRepository paymentRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
    }

    public DashboardTotals calculateTotals() {
        List<Loan> loans = loanRepository.findByStatus("ACTIVE");

        double totalLoanAmount = loans.stream().mapToDouble(Loan::getPrincipalAmount).sum();
        double totalPrincipal = loans.stream().mapToDouble(Loan::getPrincipalAmount).sum();
        double totalInterest = loans.stream().mapToDouble(Loan::getInterestAmount).sum();

        double estimatedNextWeek = loans.stream().mapToDouble(Loan::getWeeklyInstallment).sum();

        // payments
        var payments = paymentRepository.findAll();
        double principalCollected = payments.stream().mapToDouble(p -> p.getPrincipalPaid()).sum();
        double interestCollected = payments.stream().mapToDouble(p -> p.getInterestPaid()).sum();

        DashboardTotals t = new DashboardTotals();
        t.totalLoanAmount = totalLoanAmount;
        t.totalPrincipal = totalPrincipal;
        t.totalInterest = totalInterest;
        t.principalCollected = principalCollected;
        t.interestCollected = interestCollected;
        t.estimatedNextWeek = estimatedNextWeek;

        return t;
    }

    public static class DashboardTotals {
        public double totalLoanAmount;
        public double totalPrincipal;
        public double totalInterest;
        public double principalCollected;
        public double interestCollected;
        public double estimatedNextWeek;
    }
}
