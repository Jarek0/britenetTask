package com.britenet.contacts.task.validators.person;

import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class PersonReqDTOValidatorImpl implements Validator{

    private final PersonRepository personRepository;

    @Autowired
    public PersonReqDTOValidatorImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PersonReqDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Preconditions.checkNotNull(o);
        PersonReqDTO personReqDTO = (PersonReqDTO) o;

        boolean personWithThisPeselExist = personRepository.findByPesel(personReqDTO.getPesel()).orElse(null) != null;

        if(personWithThisPeselExist){
            errors.rejectValue("pesel","","validation.phoneNumber.value.exist");
        }

    }
}
