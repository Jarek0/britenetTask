package com.britenet.contacts.task.mappers.person;

import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class PersonMapperImpl implements PersonMapper{

    private final ContactMapper contactMapper;

    @Autowired
    public PersonMapperImpl(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    @Override
    public Person mapFromReqDTO(PersonReqDTO personReqDTO) {
        return Person.builder()
                .name(personReqDTO.getName())
                .surname(personReqDTO.getSurname())
                .gender(Gender.getByKind(personReqDTO.getGender()))
                .birthDate(personReqDTO.getBirthDate())
                .pesel(personReqDTO.getPesel())
                .build();
    }

    @Override
    public PersonResDTO mapToResDTO(Person person){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return PersonResDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .surname(person.getSurname())
                .gender(person.getGender().toString())
                .birthDate(person.getBirthDate().format(formatter))
                .pesel(person.getPesel())
                .build();
    }

    @Override
    public PersonWithContactsResDTO mapToResWithContactsDTO(Person person) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return PersonWithContactsResDTO.subBuilder()
                .id(person.getId())
                .name(person.getName())
                .surname(person.getSurname())
                .gender(person.getGender().toString())
                .birthDate(person.getBirthDate().format(formatter))
                .pesel(person.getPesel())
                .contacts(person.getContacts().stream().map(contactMapper::mapToResDTO).collect(Collectors.toList()))
                .build();
    }
}
