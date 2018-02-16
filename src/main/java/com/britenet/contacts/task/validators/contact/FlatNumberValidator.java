package com.britenet.contacts.task.validators.contact;

import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class FlatNumberValidator implements ConstraintValidator<FlatNumberValidation, Object> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String[] fields;

    @Override
    public void initialize(FlatNumberValidation constraintAnnotation) {
        fields = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String[] values = Stream.of(fields)
                .map(field -> PARSER.parseExpression(field).getValue(value))
                .toArray(String[]::new);
        String blockNumber = values[0];
        String flatNumber = values[1];
        if(blockNumber == null){
            String flatNumberPattern = "\\d{4}";
            return flatNumber.matches(flatNumberPattern);
        }
        else{
            String blockNumberPatter = "^\\d{5}[a-z]{1}$";
            return blockNumber.matches(blockNumberPatter);
        }

    }
}
