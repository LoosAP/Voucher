package com.loosap.voucher.service;

import com.loosap.voucher.entity.Voucher;
import com.loosap.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
        Voucher newVoucher = new Voucher("NEW", 10, LocalDateTime.of(2025, 12, 31, 23, 59, 59));
        Voucher createdVoucher = voucherService.createVoucher(newVoucher);

        assertNotNull(createdVoucher.getId());
        assertEquals("NEW", createdVoucher.getCode());
        assertEquals(10, createdVoucher.getRedemptionLimit());
        assertEquals(0, createdVoucher.getRedeemedCount());
        assertEquals(LocalDateTime.of(2025, 12, 31, 23, 59, 59), createdVoucher.getExpiryDate());

        Voucher expiredVoucher = new Voucher("EXPIRED", 5, LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            voucherService.createVoucher(expiredVoucher);
        });

        assertEquals("Expiry date must be in the future.", exception.getMessage());

    }

    @Test
    void redeemVoucherRedeemed() {
        boolean success = voucherService.redeemVoucher("REDEEMED");
        assertFalse(success);
    }
    @Test
    void redeemVoucherExpired() {
        boolean success = voucherService.redeemVoucher("EXPIRED");
        assertFalse(success);
    }

    @Test
    void redeemVoucherUnlimited() {
        boolean success = voucherService.redeemVoucher("MULTI");
        assertTrue(success);

        boolean secondAttempt = voucherService.redeemVoucher("MULTI");
        assertTrue(secondAttempt);
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