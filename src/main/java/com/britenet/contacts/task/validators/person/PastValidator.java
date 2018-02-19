package com.britenet.contacts.task.validators.person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastValidator implements ConstraintValidator<Past, Object> {

    @Override
    public void initialize(Past constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) return false;
        LocalDate validatedDate = (LocalDate) value;
        return !validatedDate.isAfter(LocalDate.now());
    }
}
