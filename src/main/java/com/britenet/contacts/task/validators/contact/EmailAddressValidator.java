package com.britenet.contacts.task.validators.contact;

import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.repositories.contact.EmailAddressRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailAddressValidator implements Validator {

    private final EmailAddressRepository emailAddressRepository;

    @Autowired
    public EmailAddressValidator(EmailAddressRepository emailAddressRepository) {
        this.emailAddressRepository = emailAddressRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EmailAddressReqDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Preconditions.checkNotNull(o);
        EmailAddressReqDTO emailAddressReqDTO = (EmailAddressReqDTO) o;

        boolean personWithThisPeselExist = emailAddressRepository.findByValue(emailAddressReqDTO.getValue()).orElse(null) != null;

        if(personWithThisPeselExist){
            errors.rejectValue("pesel","","validation.emailAddress.value.exist");
        }

    }
}
