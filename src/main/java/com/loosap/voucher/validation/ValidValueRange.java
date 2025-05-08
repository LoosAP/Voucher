package com.loosap.voucher.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ValueRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidValueRange {
    String message() default "minValue must be less than or equal to maxValue";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
