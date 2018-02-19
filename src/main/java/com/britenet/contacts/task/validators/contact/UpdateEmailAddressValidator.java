package com.britenet.contacts.task.validators.contact;

import com.britenet.contacts.task.DTO.contact.request.update.UpdateEmailAddressReqDTO;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.repositories.contact.EmailAddressRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateEmailAddressValidator implements Validator {

    private final EmailAddressRepository emailAddressRepository;

    @Autowired
    public UpdateEmailAddressValidator(EmailAddressRepository emailAddressRepository) {
        this.emailAddressRepository = emailAddressRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdateEmailAddressReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Preconditions.checkNotNull(o);
        UpdateEmailAddressReqDTO emailAddressReqDTO = (UpdateEmailAddressReqDTO) o;

        EmailAddress emailAlreadyExist = emailAddressRepository.findByValue(emailAddressReqDTO.getValue()).orElse(null);

        if(emailAlreadyExist != null && !emailAlreadyExist.getId().equals(emailAddressReqDTO.getId())){
            errors.rejectValue("","","This e-mail address already exist");
        }

    }
}
