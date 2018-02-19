package com.britenet.contacts.task.validators.contact;

import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.repositories.contact.PhoneNumberRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PhoneNumberValidator implements Validator {

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PhoneNumberValidator(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneNumberReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Preconditions.checkNotNull(o);
        PhoneNumberReqDTO phoneNumberReqDTO = (PhoneNumberReqDTO) o;

        boolean phoneNumberAlreadyExist = phoneNumberRepository.findByValue(phoneNumberReqDTO.getValue()).orElse(null) != null;

        if(phoneNumberAlreadyExist){
            errors.rejectValue("","","This phone number already exist");
        }

    }
}
