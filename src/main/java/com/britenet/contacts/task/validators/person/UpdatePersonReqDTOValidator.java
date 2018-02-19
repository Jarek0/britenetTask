package com.britenet.contacts.task.validators.person;

import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdatePersonReqDTOValidator implements Validator {

    private final PersonRepository personRepository;

    @Autowired
    public UpdatePersonReqDTOValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdatePersonReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Preconditions.checkNotNull(o);
        UpdatePersonReqDTO personReqDTO = (UpdatePersonReqDTO) o;

        Person peselAlreadyExist = personRepository.findByPesel(personReqDTO.getPesel()).orElse(null);

        if(peselAlreadyExist !=null && !peselAlreadyExist.getId().equals(personReqDTO.getId())){
            errors.rejectValue("","","Person with this pesel already exist");
        }
    }
}
