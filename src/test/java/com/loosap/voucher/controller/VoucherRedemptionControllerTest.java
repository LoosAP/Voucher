package com.loosap.voucher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loosap.voucher.service.VoucherServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class VoucherRedemptionControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @BeforeAll
    public static void setup(){
        request = new MockHttpServletRequest();
    }

    @BeforeEach
    void setUpDatabase() {
        jdbc.execute(sqlAddSingleVoucher);
        jdbc.execute(sqlAddMultipleVouchers);
        jdbc.execute(sqlAddThreeTimesVoucher);
        jdbc.execute(sqlAddExpiredVoucher);
        jdbc.execute(sqlAddRedeemedVoucher);
    }

    @Test
    void redeemVoucherWithAdminAuth() throws Exception {
        mockMvc.perform(post("/api/redeem/SINGLE")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(content().string("Voucher redeemed successfully."));
    }

    @Test
    void redeemVoucherWithUserAuth() throws Exception {
        mockMvc.perform(post("/api/redeem/SINGLE")
                .with(httpBasic("user", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("Voucher redeemed successfully."));
    }

    @Test
    void redeemVoucherWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/redeem/SINGLE"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void redeemExpiredVoucherWithAdminAuth() throws Exception {
        mockMvc.perform(post("/api/redeem/EXPIRED")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Voucher redemption failed."));
    }

    @Test
    void redeemExpiredVoucherWithUserAuth() throws Exception {
        mockMvc.perform(post("/api/redeem/EXPIRED")
                .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Voucher redemption failed."));
    }

    @Test
    void redeemVoucherExceedingLimitWithAdminAuth() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/redeem/THREETIMES")
                    .with(httpBasic("admin", "admin")))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Voucher redeemed successfully."));
        }

        mockMvc.perform(post("/api/redeem/THREETIMES")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Voucher redemption failed."));
    }


    @AfterEach
    void tearDownDatabase() {
        jdbc.execute(sqlDeleteVoucher);
    }

}