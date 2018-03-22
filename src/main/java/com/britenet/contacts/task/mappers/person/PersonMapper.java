package com.britenet.contacts.task.mappers.person;


import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.person.Person;

public interface PersonMapper {

    Person mapFromReqDTO(PersonReqDTO personReqDTO);

    PersonResDTO mapToResDTO(Person createdPerson);

    PersonWithContactsResDTO mapToResWithContactsDTO(Person person);
}
