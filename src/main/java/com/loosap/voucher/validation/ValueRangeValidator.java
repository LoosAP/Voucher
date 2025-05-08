package com.loosap.voucher.validation;

import com.loosap.voucher.entity.Voucher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueRangeValidator implements ConstraintValidator<ValidValueRange, Voucher> {

    @Override
    public boolean isValid(Voucher entity, ConstraintValidatorContext context) {
        if (entity.getRedemptionLimit() == 0) {
            return true;
        }

        if (entity.getRedeemedCount() > entity.getRedemptionLimit()) {
            // Attach the error to the 'redeemedCount' field
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Redeemed count cannot exceed redemption limit")
                    .addPropertyNode("redeemedCount")
                    .addConstraintViolation();
            return false;
        }


        return true;
    }
}