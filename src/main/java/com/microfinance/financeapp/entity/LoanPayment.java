package com.microfinance.financeapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loan_payments")
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    private LocalDate paymentDate;

    private double amountPaid;
    private double principalPaid;
    private double interestPaid;

    private int weekNumber;

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public Loan getLoan() {
        return loan;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getPrincipalPaid() {
        return principalPaid;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    // ===== SETTERS (THIS FIXES YOUR ERROR) =====

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setPrincipalPaid(double principalPaid) {
        this.principalPaid = principalPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}
