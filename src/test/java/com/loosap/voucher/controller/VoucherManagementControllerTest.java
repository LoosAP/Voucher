package com.loosap.voucher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loosap.voucher.entity.Voucher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZonedDateTime;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class VoucherManagementControllerTest {

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
    void createVoucherWithAdminAuth() throws Exception {
        Instant now = Instant.now().plusSeconds(86400);
        Voucher newVoucher = new Voucher(10, now);
        String voucherJson = objectMapper.writeValueAsString(newVoucher);

        mockMvc.perform(post("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(voucherJson)
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.redemptionLimit").value(10))
                .andExpect(jsonPath("$.expiryDate").value(now.toString()));

    }

    @Test
    void createVoucherWithoutAuth() throws Exception {
        Voucher newVoucher = new Voucher(10, Instant.now().plusSeconds(86400));
        String voucherJson = objectMapper.writeValueAsString(newVoucher);

        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(voucherJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createVoucherWithUserAuth() throws Exception {
        Voucher newVoucher = new Voucher(10, Instant.now().plusSeconds(86400));
        String voucherJson = objectMapper.writeValueAsString(newVoucher);

        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(voucherJson)
                        .with(httpBasic("user", "user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllVouchersWithAdminAuth() throws Exception {
        mockMvc.perform(get("/api/vouchers")
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getAllVouchersWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/vouchers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllVouchersWithUserAuth() throws Exception {
        mockMvc.perform(get("/api/vouchers")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void redeemExpiredVoucher() throws Exception {
        mockMvc.perform(get("/api/redeem/EXPIREDf-9a7e-45d8-b3a2-e4a3d7e2f09f")
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Voucher has expired."));
    }
    @Test
    void redeemRedeemedVoucher() throws Exception {
        mockMvc.perform(get("/api/redeem/REDEEMED-c768-40c1-bf6e-0cf1f6fa85a1")
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Voucher has reached its redemption limit."));
    }

    @Test
    void redeemNonExistentVoucher() throws Exception {
        mockMvc.perform(get("/api/redeem/EMPTY")
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Voucher not found."));
    }

    @AfterEach
    void tearDownDatabase() {
        jdbc.execute(sqlDeleteVoucher);
    }
}