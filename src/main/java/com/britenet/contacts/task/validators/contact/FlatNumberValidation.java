package com.britenet.contacts.task.validators.contact;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FlatNumberValidator.class)
public @interface FlatNumberValidation {
    String[] value();

    String message() default "{validation.address.flatNumber.pattern}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
