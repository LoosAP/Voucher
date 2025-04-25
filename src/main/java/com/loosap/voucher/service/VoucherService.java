package com.loosap.voucher.service;

import com.loosap.voucher.entity.Voucher;

import java.util.List;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    boolean redeemVoucher(String code);
    List<Voucher> getAllVouchers();
}
