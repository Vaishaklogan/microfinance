package com.microfinance.financeapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // Inputs
    private double principalAmount;
    private double interestAmount;
    private int repaymentWeeks;

    // Auto calculated
    private double weeklyInstallment;
    private double weeklyPrincipal;
    private double weeklyInterest;

    // Balances
    private double principalBalance;
    private double interestBalance;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status; // ACTIVE / PRE_CLOSED / CLOSED

    // getters & setters
    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public int getRepaymentWeeks() {
        return repaymentWeeks;
    }

    public void setRepaymentWeeks(int repaymentWeeks) {
        this.repaymentWeeks = repaymentWeeks;
    }

    public double getWeeklyInstallment() {
        return weeklyInstallment;
    }

    public void setWeeklyInstallment(double weeklyInstallment) {
        this.weeklyInstallment = weeklyInstallment;
    }

    public double getWeeklyPrincipal() {
        return weeklyPrincipal;
    }

    public void setWeeklyPrincipal(double weeklyPrincipal) {
        this.weeklyPrincipal = weeklyPrincipal;
    }

    public double getWeeklyInterest() {
        return weeklyInterest;
    }

    public void setWeeklyInterest(double weeklyInterest) {
        this.weeklyInterest = weeklyInterest;
    }

    public double getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(double principalBalance) {
        this.principalBalance = principalBalance;
    }

    public double getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(double interestBalance) {
        this.interestBalance = interestBalance;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}