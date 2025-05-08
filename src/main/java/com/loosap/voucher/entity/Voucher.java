package com.loosap.voucher.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String code;

    private int redemptionLimit;

    private int redeemedCount;

    private LocalDateTime expiryDate;

    public Voucher() {
    }

    public Voucher(int redemptionLimit, LocalDateTime expiryDate) {
        this.redemptionLimit = redemptionLimit;
        this.expiryDate = expiryDate;
        this.redeemedCount = 0;
    }

    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = UUID.randomUUID().toString();
        }
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getRedeemedCount() {
        return redeemedCount;
    }

    public void setRedeemedCount(int redeemedCount) {
        this.redeemedCount = redeemedCount;
    }

    public int getRedemptionLimit() {
        return redemptionLimit;
    }

    public void setRedemptionLimit(int redemptionLimit) {
        this.redemptionLimit = redemptionLimit;
    }

    public String getCode() {
        return code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
