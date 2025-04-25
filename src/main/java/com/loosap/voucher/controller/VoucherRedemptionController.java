package com.loosap.voucher.controller;

import com.loosap.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoucherRedemptionController {

    @Autowired
    private VoucherService voucherService;

    @RequestMapping(value = "/api/redeem/{code}", method = RequestMethod.GET)
    public ResponseEntity<String> redeemVoucher(@PathVariable String code) {
        boolean success = voucherService.redeemVoucher(code);
        if (success) {
            return ResponseEntity.ok("Voucher redeemed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Voucher redemption failed.");
        }
    }
}