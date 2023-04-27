package com.github.kangmoo.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.*;
import java.util.Locale;
import java.util.Set;

@Slf4j
public class ValidationUtil {
    protected static final Validator validator;

    static {
        Locale.setDefault(Locale.US);
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    public static void validCheck(Object obj) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            String violationMsg = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .reduce((a, b) -> a + ", " + b).orElse("");
            log.error("Validation Check fail (violations={})", violationMsg);
            throw new ValidationException(violationMsg);
        }
    }
}
