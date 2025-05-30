package com.loosap.voucher.service;

import com.loosap.voucher.entity.Voucher;
import com.loosap.voucher.repository.VoucherRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Voucher createVoucher(@Valid Voucher voucher) {
        if (voucher.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Expiry date must be in the future.");
        }

        Voucher savedVoucher = voucherRepository.save(voucher);
        return savedVoucher;
    }

    @Override
    public boolean redeemVoucher(String code) {
        return voucherRepository.findByCode(code)
                .filter(voucher -> voucher.getExpiryDate().isAfter(Instant.now()))
                .filter(voucher -> voucher.getRedemptionLimit() == 0 || voucher.getRedeemedCount() < voucher.getRedemptionLimit())
                .map(voucher -> {
                    voucher.setRedeemedCount(voucher.getRedeemedCount() + 1);
                    voucherRepository.save(voucher);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public String getRedeemDenyReason(String code) {
        return voucherRepository.findByCode(code)
                .filter(voucher -> voucher.getExpiryDate().isBefore(Instant.now()))
                .map(voucher -> "Voucher has expired.")
                .orElseGet(() -> voucherRepository.findByCode(code)
                        .filter(voucher -> voucher.getRedemptionLimit() > 0 && voucher.getRedeemedCount() >= voucher.getRedemptionLimit())
                        .map(voucher -> "Voucher has reached its redemption limit.")
                        .orElse("Voucher not found."));
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
}