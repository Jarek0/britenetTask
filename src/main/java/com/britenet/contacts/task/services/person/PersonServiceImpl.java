package com.britenet.contacts.task.services.person;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.page.PageMapper;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import com.britenet.contacts.task.repositories.contact.ContactRepository;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;
    private final ContactRepository contactRepository;

    private final PersonMapper personMapper;
    private final PageMapper pageMapper;
    private final ContactMapper contactMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             ContactRepository contactRepository,
                             PersonMapper personMapper,
                             PageMapper pageMapper, ContactMapper contactMapper) {
        this.personRepository = personRepository;
        this.contactRepository = contactRepository;
        this.personMapper = personMapper;
        this.pageMapper = pageMapper;
        this.contactMapper = contactMapper;
    }

    @Override
    public PersonResDTO createPerson(PersonReqDTO personReqDTO) {
        Person personToSave = personMapper.mapFromReqDTO(personReqDTO);

        Person createdPerson = personRepository.save(personToSave);

        return personMapper.mapToResDTO(createdPerson);
    }

    @Override
    public PersonWithContactsResDTO createContactForPerson(long personId, AddressReqDTO contactReqDTO) {
        Contact contactToAdd = contactMapper.fromReqDTO(contactReqDTO);

        return addContactToPerson(personId,contactToAdd);
    }

    @Override
    public PersonWithContactsResDTO createContactForPerson(long personId, EmailAddressReqDTO contactReqDTO) {

        Contact contactToAdd = contactMapper.fromReqDTO(contactReqDTO);

        return addContactToPerson(personId,contactToAdd);
    }

    @Override
    public PersonWithContactsResDTO createContactForPerson(long personId, PhoneNumberReqDTO contactReqDTO) {
        Contact contactToAdd = contactMapper.fromReqDTO(contactReqDTO);

        return addContactToPerson(personId,contactToAdd);
    }

    @Override
    public List<PersonResDTO> readAllPersons() {

        List<Person> allPersons = personRepository.findAll();

        return allPersons.stream().map(personMapper::mapToResDTO).collect(Collectors.toList());
    }

    @Override
    public PageResDTO<PersonResDTO> readPageOfPersons(int number, int size, String sortedBy, String order) {

        PageRequest pageRequest = new PageRequest(number,size, Sort.Direction.fromString(order),sortedBy);
        Page<Person> pageOfPersons = personRepository.findAll(pageRequest);

        return pageMapper.mapToResDTO(pageOfPersons,personMapper::mapToResDTO);
    }

    @Override
    public List<PersonWithContactsResDTO> readAllPersonsWithContacts() {

        List<Person> allPersonsWithContacts = personRepository.findAllWithContacts();

        return allPersonsWithContacts.stream().map(personMapper::mapToResWithContactsDTO).collect(Collectors.toList());
    }

    @Override
    public PageResDTO<PersonWithContactsResDTO> readPageOfPersonsWithContacts(int number, int size, String sortedBy, String order) {

        PageRequest pageRequest = new PageRequest(number,size, Sort.Direction.fromString(order),sortedBy);
        Page<Person> pageOfPersons = personRepository.findPageWithContacts(pageRequest);

        return pageMapper.mapToResDTO(pageOfPersons,personMapper::mapToResWithContactsDTO);
    }

    @Override
    public PersonResDTO readPerson(long personId) {

        Person foundPerson = personRepository.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));

        return personMapper.mapToResDTO(foundPerson);
    }

    @Override
    public PersonResDTO readPersonByPesel(String pesel) {

        Person foundPerson = personRepository.findByPesel(pesel)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,"pesel",pesel));

        return personMapper.mapToResDTO(foundPerson);
    }

    @Override
    public PersonWithContactsResDTO readPersonWithContacts(long personId) {

        Person foundPerson = personRepository.findByIdWithContacts(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));

        return personMapper.mapToResWithContactsDTO(foundPerson);
    }

    @Override
    public PersonResDTO readPersonByPeselWithContacts(String pesel) {
        Person foundPerson = personRepository.findByPeselWithContacts(pesel)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,"pesel",pesel));

        return personMapper.mapToResWithContactsDTO(foundPerson);
    }

    @Override
    public PersonResDTO updatePerson(UpdatePersonReqDTO personReqDTO) {

        Person personToUpdate = personRepository.findById(personReqDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));

        personToUpdate.setName(personReqDTO.getName());
        personToUpdate.setSurname(personReqDTO.getSurname());
        personToUpdate.setGender(Gender.getByKind(personReqDTO.getGender()));
        personToUpdate.setBirthDate(personReqDTO.getBirthDate());
        personToUpdate.setPesel(personReqDTO.getPesel());

        personRepository.save(personToUpdate);

        return personMapper.mapToResDTO(personToUpdate);
    }

    @Override
    public void deletePerson(long personId) {
        personRepository.delete(personId);
    }

    @Override
    public void deleteAllPersons() {
        personRepository.deleteAll();
    }

    private PersonWithContactsResDTO addContactToPerson(long personId, Contact contactToAdd){

        Person person = personRepository.findByIdWithContacts(personId)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class));
        person.addContact(contactToAdd);

        contactRepository.save(contactToAdd);

        return personMapper.mapToResWithContactsDTO(person);
    }
}
