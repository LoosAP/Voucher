package com.loosap.voucher.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoucherValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testRedeemedCountExceedsRedemptionLimit() {
        Voucher voucher = new Voucher(1, LocalDateTime.now().plusDays(1));
        voucher.setRedeemedCount(2);

        Set<ConstraintViolation<Voucher>> violations = validator.validate(voucher);
        assertEquals(1, violations.size());
        assertEquals("Redeemed count cannot exceed redemption limit", violations.iterator().next().getMessage());
    }

    @Test
    void testRedeemedCountEqualsRedemptionLimit() {
        Voucher voucher = new Voucher(10, LocalDateTime.now().plusDays(1));
        voucher.setRedeemedCount(10);

        Set<ConstraintViolation<Voucher>> violations = validator.validate(voucher);
        assertEquals(0, violations.size());
    }

    @Test
    void testZeroRedemptionLimitAndRedeemedCount() {
        Voucher voucher = new Voucher(0, LocalDateTime.now().plusDays(1));
        voucher.setRedeemedCount(1);

        Set<ConstraintViolation<Voucher>> violations = validator.validate(voucher);
        assertEquals(0, violations.size());
    }
}