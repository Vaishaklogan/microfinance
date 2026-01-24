package com.microfinance.financeapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double loanAmount;
    private double principalAmount;
    private double interestAmount;
    private int totalWeeks;
    private int repaymentWeeks;
    private double weeklyInstallment;
    private double weeklyPrincipal;
    private double weeklyInterest;
    private double principalBalance;
    private double interestBalance;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
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

    public int getTotalWeeks() {
        return totalWeeks;
    }

    public void setTotalWeeks(int totalWeeks) {
        this.totalWeeks = totalWeeks;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
