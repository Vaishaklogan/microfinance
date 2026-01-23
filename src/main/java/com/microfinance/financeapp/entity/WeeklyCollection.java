package com.microfinance.financeapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "weekly_collections")
public class WeeklyCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Loan loan;

    private LocalDate collectionDate;
    private double amountPaid;

    private double principalPaid;
    private double interestPaid;

    // getters & setters
    public Long getId() {
        return id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(double principalPaid) {
        this.principalPaid = principalPaid;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }
}
