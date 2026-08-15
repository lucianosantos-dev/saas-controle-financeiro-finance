package com.lucianodev.saascontrolefinanceirofinance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SenhaForteValidation.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaForte {

    String message() default "A senha não atende aos requisitos de segurança";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}
