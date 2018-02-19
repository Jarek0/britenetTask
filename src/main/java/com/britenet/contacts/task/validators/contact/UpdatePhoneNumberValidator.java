package com.britenet.contacts.task.validators.contact;

import com.britenet.contacts.task.DTO.contact.request.update.UpdatePhoneNumberReqDTO;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.repositories.contact.PhoneNumberRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdatePhoneNumberValidator implements Validator {

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public UpdatePhoneNumberValidator(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdatePhoneNumberReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Preconditions.checkNotNull(o);
        UpdatePhoneNumberReqDTO phoneNumberReqDTO = (UpdatePhoneNumberReqDTO) o;

        PhoneNumber phoneNumberAlreadyExist = phoneNumberRepository.findByValue(phoneNumberReqDTO.getValue()).orElse(null);

        if(phoneNumberAlreadyExist != null && !phoneNumberAlreadyExist.getId().equals(phoneNumberReqDTO.getId())){
            errors.rejectValue("","","This phone number already exist");
        }

    }
}
