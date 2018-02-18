package com.britenet.contacts.task.unit.services;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.page.PageMapper;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import com.britenet.contacts.task.services.person.PersonService;
import com.britenet.contacts.task.services.person.PersonServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressReqDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private PageMapper pageMapper;
    @Mock
    private ContactMapper contactMapper;

    private PersonService personService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @Before
    public void init(){
        personService = new PersonServiceImpl(
                personRepository,
                personMapper,
                pageMapper,
                contactMapper
        );
    }

    @Test
    public void whenICreatePerson_thenThisPersonIsSavedAndMapped(){
        //given
        PersonReqDTO jarekReqDTO = createJarekReqDTO();
        Person jarek = createJarek();

        when(personMapper.mapFromReqDTO(jarekReqDTO)).thenReturn(jarek);

        Person savedJarek = createJarek();
        savedJarek.setId(1L);

        when(personRepository.save(jarek)).thenReturn(savedJarek);

        //when
        personService.createPerson(jarekReqDTO);

        //then
        verify(personMapper,times(1)).mapFromReqDTO(jarekReqDTO);
        verify(personRepository,times(1)).save(jarek);
        verify(personMapper,times(1)).mapToResDTO(savedJarek);
    }

    @Test
    public void whenICreateAddressForPerson_thenPersonIsSavedWithThisAddressAndMapped(){
        //given
        AddressReqDTO addressReqDTO = createTestAddressReqDTO();
        Address address = (Address) createTestAddress();
        Person jarek = createJarek();

        when(contactMapper.fromReqDTO(addressReqDTO)).thenReturn(address);
        when(personRepository.findByIdWithContacts(1L)).thenReturn(Optional.of(jarek));

        //when
        personService.createContactForPerson(1L,addressReqDTO);

        //then
        verify(contactMapper,times(1)).fromReqDTO(addressReqDTO);
        verify(personRepository,times(1)).findByIdWithContacts(1L);
        verify(personRepository,times(1)).save(personArgumentCaptor.capture());
        verify(personMapper,times(1)).mapToResWithContactsDTO(jarek);

        Person capturedPerson = personArgumentCaptor.getValue();
        assertEquals(capturedPerson,jarek);
        assertTrue(jarek.getContacts().contains(address));
    }

    @Test
    public void whenICreateEmailAddressForPerson_thenPersonIsSavedWithThisEmailAddressAndMapped(){
        //given
        EmailAddressReqDTO emailAddressReqDTO = new EmailAddressReqDTO("jery0@o2.pl");
        EmailAddress emailAddress = (EmailAddress) createTestEmailAddress();
        Person jarek = createJarek();

        when(contactMapper.fromReqDTO(emailAddressReqDTO)).thenReturn(emailAddress);
        when(personRepository.findByIdWithContacts(1L)).thenReturn(Optional.of(jarek));

        //when
        personService.createContactForPerson(1L,emailAddressReqDTO);

        //then
        verify(contactMapper,times(1)).fromReqDTO(emailAddressReqDTO);
        verify(personRepository,times(1)).findByIdWithContacts(1L);
        verify(personRepository,times(1)).save(personArgumentCaptor.capture());
        verify(personMapper,times(1)).mapToResWithContactsDTO(jarek);

        Person capturedPerson = personArgumentCaptor.getValue();
        assertEquals(capturedPerson,jarek);
        assertTrue(jarek.getContacts().contains(emailAddress));
    }

    @Test
    public void whenICreatePhoneNumberForPerson_thenPersonIsSavedWithThisPhoneNumberAndMapped(){
        //given
        PhoneNumberReqDTO phoneNumberReqDTO = new PhoneNumberReqDTO("999999999");
        PhoneNumber phoneNumber = (PhoneNumber) createTestPhoneNumber();
        Person jarek = createJarek();

        when(contactMapper.fromReqDTO(phoneNumberReqDTO)).thenReturn(phoneNumber);
        when(personRepository.findByIdWithContacts(1L)).thenReturn(Optional.of(jarek));

        //when
        personService.createContactForPerson(1L,phoneNumberReqDTO);

        //then
        verify(contactMapper,times(1)).fromReqDTO(phoneNumberReqDTO);
        verify(personRepository,times(1)).findByIdWithContacts(1L);
        verify(personRepository,times(1)).save(personArgumentCaptor.capture());
        verify(personMapper,times(1)).mapToResWithContactsDTO(jarek);

        Person capturedPerson = personArgumentCaptor.getValue();
        assertEquals(capturedPerson,jarek);
        assertTrue(jarek.getContacts().contains(phoneNumber));
    }

    @Test
    public void whenIReadSomeSavedPersons_thenTheyAreReadAndMapped(){
        //given
        Person jarek = createJarek();
        Person adam = createAdam();
        Person adrian = createAdrian();

        List<Person> people = Arrays.asList(jarek,adam,adrian);

        when(personRepository.findAll()).thenReturn(people);

        //when
        personService.readAllPersons();

        //then
        verify(personRepository,times(1)).findAll();
        verify(personMapper,times(1)).mapToResDTO(jarek);
        verify(personMapper,times(1)).mapToResDTO(adam);
        verify(personMapper,times(1)).mapToResDTO(adrian);
    }

    @Test
    public void whenIReadPageOfSavedPersons_thenPageIsReadAndMapped(){
        //given
        PageRequest pageRequest = new PageRequest(2,3, Sort.Direction.ASC,"kind");
        Person jarek = createJarek();
        Person adam = createAdam();
        Person adrian = createAdrian();
        Page<Person> page = new PageImpl<>(Arrays.asList(jarek,adam,adrian),pageRequest,3);

        when(personRepository.findAll(pageRequest)).thenReturn(page);

        //when
        personService.readPageOfPersons(2,3,"kind","ASC");

        //then
        verify(personRepository, times(1)).findAll(pageRequest);
        verify(pageMapper, times(1)).mapToResDTO(any(),any());
    }

    @Test
    public void whenIReadSomeSavedPersonsWithContacts_thenTheyAreReadAndMappedWithTheirContacts(){
        //given
        Person jarek = createJarek();
        jarek.addContact(new PhoneNumber("999999999"));
        jarek.addContact(new EmailAddress("jery0@o2.pl"));
        Person adam = createAdam();
        adam.addContact(new PhoneNumber("999999996"));
        adam.addContact(new EmailAddress("jery2@o2.pl"));
        Person adrian = createAdrian();
        adrian.addContact(new PhoneNumber("999999956"));
        adrian.addContact(new EmailAddress("jery5@o2.pl"));

        List<Person> people = Arrays.asList(jarek,adam,adrian);

        when(personRepository.findAllWithContacts()).thenReturn(people);

        //when
        personService.readAllPersonsWithContacts();

        //then
        verify(personRepository,times(1)).findAllWithContacts();
        verify(personMapper,times(1)).mapToResWithContactsDTO(jarek);
        verify(personMapper,times(1)).mapToResWithContactsDTO(adam);
        verify(personMapper,times(1)).mapToResWithContactsDTO(adrian);
    }

    @Test
    public void whenIReadSomePageOfSavedPersons_thenPageIsReadAndMapped(){
        //given
        PageRequest pageRequest = new PageRequest(2,3, Sort.Direction.ASC,"kind");
        Person jarek = createJarek();
        jarek.addContact(new PhoneNumber("999999999"));
        jarek.addContact(new EmailAddress("jery0@o2.pl"));
        Person adam = createAdam();
        adam.addContact(new PhoneNumber("999999996"));
        adam.addContact(new EmailAddress("jery2@o2.pl"));
        Person adrian = createAdrian();
        adrian.addContact(new PhoneNumber("999999956"));
        adrian.addContact(new EmailAddress("jery5@o2.pl"));
        Page<Person> page = new PageImpl<>(Arrays.asList(jarek,adam,adrian),pageRequest,3);

        when(personRepository.findPageWithContacts(pageRequest)).thenReturn(page);

        //when
        personService.readPageOfPersonsWithContacts(2,3,"kind","ASC");

        //then
        verify(personRepository, times(1)).findPageWithContacts(pageRequest);
        verify(pageMapper, times(1)).mapToResDTO(any(),any());
    }

    @Test
    public void whenIReadSavedPersonById_thenThisPersonIsReadAndMapped(){
        //given
        Person foundPerson = createJarek();
        foundPerson.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(foundPerson));

        //when
        personService.readPerson(1L);

        verify(personRepository,times(1)).findById(1);
        verify(personMapper,times(1)).mapToResDTO(foundPerson);
    }

    @Test
    public void whenIReadNotExistingPersonById_thenIGetObjectNotFoundException(){
        //then
        expectedException.expect(ObjectNotFoundException.class);
        expectedException.expectMessage("Person not found");

        //given
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        when(personMapper.mapToResDTO(any(Person.class))).thenReturn(any(PersonResDTO.class));

        //then
        personService.readPerson(1L);
    }

    @Test
    public void whenIReadSavedPersonByPesel_thenThisPersonIsReadAndMapped(){
        //given
        Person foundPerson = createJarek();
        foundPerson.setId(1L);

        when(personRepository.findByPesel("99999999999")).thenReturn(Optional.of(foundPerson));

        //when
        personService.readPersonByPesel("99999999999");

        verify(personRepository,times(1)).findByPesel("99999999999");
        verify(personMapper,times(1)).mapToResDTO(foundPerson);
    }

    @Test
    public void whenIReadSavedPersonWithContacts_thenThisPersonIsReadAndMappedWithContacts(){
        //given
        Person foundPerson = createJarek();
        foundPerson.addContact(createTestPhoneNumber());
        foundPerson.addContact(createTestEmailAddress());
        foundPerson.addContact(createTestAddress());

        when(personRepository.findByIdWithContacts(1L)).thenReturn(Optional.of(foundPerson));

        //when
        personService.readPersonWithContacts(1L);

        verify(personRepository,times(1)).findByIdWithContacts(1);
        verify(personMapper,times(1)).mapToResWithContactsDTO(foundPerson);
    }

    @Test
    public void whenIReadSavedPersonWithContactsByPesel_thenThisPersonIsReadAndMappedWithContacts(){
        //given
        Person foundPerson = createJarek();
        foundPerson.addContact(createTestPhoneNumber());
        foundPerson.addContact(createTestEmailAddress());
        foundPerson.addContact(createTestAddress());

        when(personRepository.findByPeselWithContacts("99999999999")).thenReturn(Optional.of(foundPerson));

        //when
        personService.readPersonByPeselWithContacts("99999999999");

        verify(personRepository,times(1)).findByPeselWithContacts("99999999999");
        verify(personMapper,times(1)).mapToResWithContactsDTO(foundPerson);
    }

    @Test
    public void whenIUpdateSavedPerson_thenThisPersonIsUpdatedAndMapped(){
        //given
        Person jarek = createJarek();

        UpdatePersonReqDTO updatePersonReqDTO = UpdatePersonReqDTO.subBuilder()
                .id(1L)
                .name("Jurek")
                .surname("Kowalski")
                .gender("male")
                .pesel("99999999977")
                .birthDate(LocalDate.parse("2014-01-01"))
                .build();

        when(personRepository.findById(1L)).thenReturn(Optional.of(jarek));

        //when
        personService.updatePerson(updatePersonReqDTO);

        //then
        verify(personRepository,times(1)).findById(1L);
        verify(personMapper,times(1)).mapToResDTO(personArgumentCaptor.capture());

        Person updatedPerson = personArgumentCaptor.getValue();
        Person expectedPerson = Person.builder()
                .name("Jurek")
                .surname("Kowalski")
                .gender(Gender.MALE)
                .pesel("99999999977")
                .birthDate(LocalDate.parse("2014-01-01"))
                .build();

        assertEquals(expectedPerson,updatedPerson);
        assertEquals(expectedPerson.getName(),updatedPerson.getName());
        assertEquals(expectedPerson.getSurname(),updatedPerson.getSurname());
        assertEquals(expectedPerson.getGender(),updatedPerson.getGender());
        assertEquals(expectedPerson.getPesel(),updatedPerson.getPesel());
        assertEquals(expectedPerson.getBirthDate(),updatedPerson.getBirthDate());
    }

    @Test
    public void whenIDeleteSavedPerson_thenItIsDeleted(){
        //when
        personService.deletePerson(1L);

        //then
        verify(personRepository, times(1)).delete(1L);
    }

    @Test
    public void whenIDeleteAllPersons_thenTheyAreDeleted(){
        //when
        personService.deleteAllPersons();

        //then
        verify(personRepository, times(1)).deleteAll();
    }
}
