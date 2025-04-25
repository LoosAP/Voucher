package com.loosap.voucher.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private int redemptionLimit;

    private int redeemedCount;

    private LocalDateTime expiryDate;

    public Voucher() {
    }

    public Voucher(String code, int redemptionLimit, LocalDateTime expiryDate) {
        this.code = code;
        this.redemptionLimit = redemptionLimit;
        this.expiryDate = expiryDate;
        this.redeemedCount = 0;
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

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
