package com.britenet.contacts.task.services.person;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;

import java.util.List;


public interface PersonService {

    PersonResDTO createPerson(PersonReqDTO personDTO);

    PersonWithContactsResDTO createContactForPerson(long personId, AddressReqDTO contactReqDTO);

    PersonWithContactsResDTO createContactForPerson(long personId, EmailAddressReqDTO contactReqDTO);

    PersonWithContactsResDTO createContactForPerson(long personId, PhoneNumberReqDTO contactReqDTO);

    List<PersonResDTO> readAllPersons();

    PageResDTO<PersonResDTO> readPageOfPersons(int number, int size, String sortedBy, String order);

    List<PersonWithContactsResDTO> readAllPersonsWithContacts();

    PageResDTO<PersonWithContactsResDTO> readPageOfPersonsWithContacts(int number, int size, String sortedBy, String order);

    PersonResDTO readPerson(long personId);

    PersonResDTO readPersonByPesel(String pesel);

    PersonWithContactsResDTO readPersonWithContacts(long personId);

    PersonResDTO readPersonByPeselWithContacts(String pesel);

    PersonResDTO updatePerson(UpdatePersonReqDTO updatePersonReqDTO);

    void deletePerson(long personId);

    void deleteAllPersons();
}
