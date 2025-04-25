package com.loosap.voucher.controller;

import com.loosap.voucher.entity.Voucher;
import com.loosap.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoucherManagementController {

    @Autowired
    private VoucherService voucherService;

    @RequestMapping(value = "/api/vouchers", method = RequestMethod.POST)
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher request) {
        Voucher response = voucherService.createVoucher(request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/vouchers", method = RequestMethod.GET)
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        System.out.println("Vouchers: " + vouchers);
        return ResponseEntity.ok(vouchers);
    }
}
