package com.microfinance.financeapp.service;

import com.microfinance.financeapp.entity.Loan;
import com.microfinance.financeapp.entity.LoanPayment;
import com.microfinance.financeapp.repository.LoanPaymentRepository;
import com.microfinance.financeapp.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanPaymentService {

    private final LoanRepository loanRepo;
    private final LoanPaymentRepository paymentRepo;

    public LoanPaymentService(LoanRepository loanRepo,
            LoanPaymentRepository paymentRepo) {
        this.loanRepo = loanRepo;
        this.paymentRepo = paymentRepo;
    }

    public void makeWeeklyPayment(Long loanId) {

        Loan loan = loanRepo.findById(loanId).orElseThrow();

        LoanPayment payment = new LoanPayment();
        payment.setLoan(loan);
        payment.setPaymentDate(LocalDate.now());
        payment.setAmountPaid(loan.getWeeklyInstallment());
        payment.setPrincipalPaid(loan.getWeeklyPrincipal());
        payment.setInterestPaid(loan.getWeeklyInterest());

        int weekNo = (int) ((loan.getStartDate().until(LocalDate.now()).getDays()) / 7) + 1;
        payment.setWeekNumber(weekNo);

        // update balances
        loan.setPrincipalBalance(
                loan.getPrincipalBalance() - loan.getWeeklyPrincipal());
        loan.setInterestBalance(
                loan.getInterestBalance() - loan.getWeeklyInterest());

        paymentRepo.save(payment);
        loanRepo.save(loan);
    }
}
