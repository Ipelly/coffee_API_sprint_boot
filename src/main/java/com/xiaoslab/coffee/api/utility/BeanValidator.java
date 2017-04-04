package com.xiaoslab.coffee.api.utility;

import org.springframework.util.CollectionUtils;

import javax.validation.*;
import java.util.Set;

public class BeanValidator {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> errors = VALIDATOR.validate(object);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ConstraintViolationException(errors);
        }
    }
}
