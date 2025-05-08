package com.loosap.voucher.service;

import com.loosap.voucher.entity.Voucher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

@TestPropertySource("/application-test.properties")
@SpringBootTest
class VoucherServiceImplTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private VoucherServiceImpl voucherService;

    @Value("${sql.script.create.single}")
    private String sqlAddSingleVoucher;

    @Value("${sql.script.create.multiple}")
    private String sqlAddMultipleVouchers;

    @Value("${sql.script.create.threetimes}")
    private String sqlAddThreeTimesVoucher;

    @Value("${sql.script.create.expired}")
    private String sqlAddExpiredVoucher;

    @Value("${sql.script.create.redeemed}")
    private String sqlAddRedeemedVoucher;

    @Value("${sql.script.delete.voucher}")
    private String sqlDeleteVoucher;

    @BeforeEach
    void setUpDatabase() {
        jdbc.execute(sqlAddSingleVoucher);
        jdbc.execute(sqlAddMultipleVouchers);
        jdbc.execute(sqlAddThreeTimesVoucher);
        jdbc.execute(sqlAddExpiredVoucher);
        jdbc.execute(sqlAddRedeemedVoucher);
    }

    @Test
    void createVoucher() {
        Instant now = Instant.now().plusSeconds(86400);
        Voucher newVoucher = new Voucher(10, now);
        Voucher createdVoucher = voucherService.createVoucher(newVoucher);

        assertNotNull(createdVoucher.getId());
        assertEquals(10, createdVoucher.getRedemptionLimit());
        assertEquals(0, createdVoucher.getRedeemedCount());
        assertEquals(now, createdVoucher.getExpiryDate());

        Voucher expiredVoucher = new Voucher(5, Instant.now().minusSeconds(86400));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            voucherService.createVoucher(expiredVoucher);
        });

        assertEquals("Expiry date must be in the future.", exception.getMessage());

    }

    @Test
    void redeemVoucherRedeemed() {
        boolean success = voucherService.redeemVoucher("REDEEMED-c768-40c1-bf6e-0cf1f6fa85a1");
        assertFalse(success);
    }
    @Test
    void redeemVoucherExpired() {
        boolean success = voucherService.redeemVoucher("EXPIREDf-9a7e-45d8-b3a2-e4a3d7e2f09f");
        assertFalse(success);
    }

    @Test
    void redeemVoucherUnlimited() {
        boolean success = voucherService.redeemVoucher("MULTIe67-0ac4-4b2a-b511-6bde144f4f12");
        assertTrue(success);

        boolean secondAttempt = voucherService.redeemVoucher("MULTIe67-0ac4-4b2a-b511-6bde144f4f12");
        assertTrue(secondAttempt);
    }

    @Test
    void redeemVoucherSingle() {
        boolean success = voucherService.redeemVoucher("SINGLE6a-9d7e-42a1-8a59-59cb91fbc2d4");
        assertTrue(success);

        boolean secondAttempt = voucherService.redeemVoucher("SINGLE6a-9d7e-42a1-8a59-59cb91fbc2d4");
        assertFalse(secondAttempt);
    }

    @Test
    void getAllVouchers() {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        assertNotNull(vouchers);
        assertEquals(5, vouchers.size());
    }
    
    @AfterEach
    void tearDownDatabase() {
        jdbc.execute(sqlDeleteVoucher);
    }
}