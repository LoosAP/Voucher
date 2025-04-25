package com.loosap.voucher.service;

import com.loosap.voucher.entity.Voucher;
import com.loosap.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        if (voucher.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiry date must be in the future.");
        }

        Voucher savedVoucher = voucherRepository.save(voucher);
        return voucherRepository.save(voucher);
    }

    @Override
    public boolean redeemVoucher(String code) {
        return voucherRepository.findByCode(code)
                .filter(voucher -> voucher.getExpiryDate().isAfter(LocalDateTime.now()))
                .filter(voucher -> voucher.getRedemptionLimit() == 0 || voucher.getRedeemedCount() < voucher.getRedemptionLimit())
                .map(voucher -> {
                    voucher.setRedeemedCount(voucher.getRedeemedCount() + 1);
                    voucherRepository.save(voucher);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
}