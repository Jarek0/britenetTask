package com.britenet.contacts.task.validators.contact;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class FlatNumberValidator implements ConstraintValidator<FlatNumberValidation, Object> {

    private String flatNumber;
    private String blockNumber;

    @Override
    public void initialize(FlatNumberValidation constraintAnnotation) {
        this.flatNumber = constraintAnnotation.field();
        this.blockNumber = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        String flatNumber = (String)(new BeanWrapperImpl(value).getPropertyValue(this.flatNumber));
        String blockNumber = (String)(new BeanWrapperImpl(value).getPropertyValue(this.blockNumber));

        if(flatNumber == null) return false;

        if(blockNumber != null){
            String flatNumberPattern = "\\d{1,5}";
            if(!flatNumber.matches(flatNumberPattern)){
                this.displayAsFieldError(context);
                return false;
            }
        }
        else{
            String flatNumberPattern = "^\\d{1,5}[a-z]?$";
            if(!flatNumber.matches(flatNumberPattern)){
                this.displayAsFieldError(context);
                return false;
            }
        }
        return true;
    }

    private void displayAsFieldError(ConstraintValidatorContext context){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Invalid flat number format").addPropertyNode("flatNumber").addConstraintViolation();

    }
}
