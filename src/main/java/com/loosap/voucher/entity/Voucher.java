package com.loosap.voucher.entity;

import com.loosap.voucher.validation.ValidValueRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@ValidValueRange(message = "Redeemed count cannot exceed redemption limit")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String code;

    @Min(value = 0, message = "Redemption limit must be greater than or equal to 0")
    @Max(value = 1000, message = "Redemption limit must be less than or equal to 1000")
    private int redemptionLimit;

    @Min(value = 0, message = "Redeemed count must be greater than or equal to 0")
    @Max(value = 1000, message = "Redeemed count must be less than or equal to 1000")
    private int redeemedCount;

    @Future(message = "Expiry date must be in the future")
    private ZonedDateTime expiryDate;

    public Voucher() {
    }

    public Voucher(int redemptionLimit, ZonedDateTime expiryDate) {
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

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(ZonedDateTime expiryDate) {
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
