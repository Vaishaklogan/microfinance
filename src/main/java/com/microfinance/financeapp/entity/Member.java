package com.microfinance.financeapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String memberCode; // Unique ID like MEM-2026-XXXXX

    private String name;
    private String aadhaar;
    private String address;
    private String landmark;

    private String status = "ACTIVE"; // ACTIVE / INACTIVE

    @ManyToOne
    private Group group;

    private LocalDateTime createdAt;

    // Constructor
    public Member() {
        this.memberCode = generateMemberCode();
        this.createdAt = LocalDateTime.now();
    }

    // Generate unique member code
    private static String generateMemberCode() {
        String year = String.valueOf(java.time.Year.now().getValue());
        String randomCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "MEM-" + year + "-" + randomCode;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
