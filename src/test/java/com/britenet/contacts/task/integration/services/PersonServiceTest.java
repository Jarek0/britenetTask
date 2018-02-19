package com.britenet.contacts.task.integration.services;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.contact.ContactRepository;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import com.britenet.contacts.task.services.person.PersonService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressReqDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PersonService personService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void clearDataBaseBeforeEachTest(){
        personRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    public void whenICreatePerson_thenThisPersonIsSavedAndMappedToPersonResDTO(){
        //when
        PersonReqDTO createJarekReqDTO = createJarekReqDTO();
        PersonResDTO jarekResDTO = personService.createPerson(createJarekReqDTO);

        entityManager.clear();

        //then
        PersonResDTO expectedPersonResDTO = createJarekResDTO();
        assertEquals(expectedPersonResDTO,jarekResDTO);
        Person expectedPerson = createJarek();
        assertTrue(personRepository.findAll().contains(expectedPerson));
    }

    @Test
    public void whenICreateAddressForPerson_thenThisContactIsSavedAndMappedToPersonWithContactResDTO(){
        //given
        Person jarek = createJarek();
        personRepository.save(jarek);

        entityManager.clear();

        //when
        AddressReqDTO addressReqDTO = createTestAddressReqDTO();
        PersonWithContactsResDTO resDTO = personService.createContactForPerson(jarek.getId(),addressReqDTO);

        entityManager.clear();

        //then
        Address expectedAddress = (Address) createTestAddress();
        assertTrue(personRepository.findByPeselWithContacts(jarek.getPesel()).get().getContacts().contains(expectedAddress));
        assertTrue(contactRepository.findAll().contains(expectedAddress));

        ContactResDTO contactResDTO = createTestAddressResDTO();

        PersonWithContactsResDTO expectedPersonResDTO = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Collections.singletonList(contactResDTO))
                .build();

        assertEquals(expectedPersonResDTO,resDTO);
    }

    @Test
    public void whenICreateEmailAddressForPerson_thenThisContactIsSavedAndMappedToPersonWithContactResDTO(){
        //given
        Person jarek = createJarek();
        personRepository.save(jarek);

        entityManager.clear();

        //when
        EmailAddressReqDTO emailAddressReqDTO = new EmailAddressReqDTO("jarek@o2.pl");
        PersonWithContactsResDTO resDTO = personService.createContactForPerson(jarek.getId(),emailAddressReqDTO);

        entityManager.clear();

        //then
        EmailAddress expectedEmailAddress = new EmailAddress("jarek@o2.pl");
        assertTrue(personRepository.findByPeselWithContacts(jarek.getPesel()).get().getContacts().contains(expectedEmailAddress));
        assertTrue(contactRepository.findAll().contains(expectedEmailAddress));

        ContactResDTO contactResDTO = ContactResDTO.builder()
                .id(1L)
                .kind("e-mail address")
                .value("jarek@o2.pl")
                .build();

        PersonWithContactsResDTO expectedPersonResDTO = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Collections.singletonList(contactResDTO))
                .build();

        assertEquals(expectedPersonResDTO,resDTO);
    }

    @Test
    public void whenICreatePhoneNumberForPerson_thenThisContactIsSavedAndMappedToPersonWithContactResDTO(){
        //given
        Person jarek = createJarek();
        personRepository.save(jarek);

        entityManager.clear();

        //when
        PhoneNumberReqDTO phoneNumberReqDTO = new PhoneNumberReqDTO("123123123");
        PersonWithContactsResDTO resDTO = personService.createContactForPerson(jarek.getId(),phoneNumberReqDTO);

        entityManager.clear();

        //then
        PhoneNumber expectedPhoneNumber = new PhoneNumber("123123123");
        assertTrue(personRepository.findByPeselWithContacts(jarek.getPesel()).get().getContacts().contains(expectedPhoneNumber));
        assertTrue(contactRepository.findAll().contains(expectedPhoneNumber));

        ContactResDTO contactResDTO = ContactResDTO.builder()
                .id(1L)
                .kind("phone number")
                .value("123123123")
                .build();

        PersonWithContactsResDTO expectedPersonResDTO = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Collections.singletonList(contactResDTO))
                .build();

        assertEquals(expectedPersonResDTO,resDTO);
    }

    @Test
    public void whenIReadSomeSavedPersons_thenIGetListOfPersons(){
        //given
        Person jarek = createJarek();
        Person adam = createAdam();
        Person adrian = createAdrian();

        personRepository.save(Arrays.asList(jarek,adam,adrian));

        entityManager.clear();

        //when
        List<PersonResDTO> persons = personService.readAllPersons();

        //then
        PersonResDTO expectedPerson1 = createJarekResDTO();
        PersonResDTO expectedPerson2 = createAdamResDTO();
        PersonResDTO expectedPerson3 = createAdrianResDTO();

        List<PersonResDTO> expectedPersons = Arrays.asList(expectedPerson1,expectedPerson2,expectedPerson3);

        assertTrue(persons.containsAll(expectedPersons));
    }

    @Test
    public void whenIReadSavedPersonById_thenIGetPersonResDTO(){
        //given
        Person jarek = createJarek();
        personRepository.save(jarek);

        entityManager.clear();

        //when
        PersonResDTO personResDTO = personService.readPerson(jarek.getId());

        //then
        PersonResDTO expectedPersonResDTO = createJarekResDTO();
        assertEquals(expectedPersonResDTO,personResDTO);
    }

    @Test
    public void whenIReadSavedPersonByPesel_thenIGetPersonResDTO(){
        //given
        Person jarek = createJarek();
        personRepository.save(jarek);

        entityManager.clear();

        //when
        PersonResDTO personResDTO = personService.readPersonByPesel(jarek.getPesel());

        //then
        PersonResDTO expectedPersonResDTO = createJarekResDTO();
        assertEquals(expectedPersonResDTO,personResDTO);
    }

    @Test
    public void whenIUpdateSavedPerson_thenIGetPersonResDTOAndPersonIsUpdated(){
        //given
        Person jarek = createJarek();
        personRepository.save(jarek);

        entityManager.clear();

        //when
        UpdatePersonReqDTO personReqDTO = UpdatePersonReqDTO.subBuilder()
                .id(jarek.getId())
                .name("Andrzej")
                .surname("Kowalski")
                .gender("male")
                .birthDate(LocalDate.parse("2015-01-01"))
                .pesel("12312312333")
                .build();

        PersonResDTO personResDTO = personService.updatePerson(personReqDTO);

        //then
        PersonResDTO expectedPersonResDTO = PersonResDTO.builder()
                .id(jarek.getId())
                .name("Andrzej")
                .surname("Kowalski")
                .gender("male")
                .birthDate("2015-01-01")
                .pesel("12312312333")
                .build();
        assertEquals(expectedPersonResDTO,personResDTO);

        Person expectedPerson = Person.builder()
                .pesel("12312312333")
                .build();
        assertTrue(personRepository.findById(jarek.getId()).get().equals(expectedPerson));
    }

    @Test
    public void whenIDeleteSavedPersonAndTryToFindIt_thenIGetException(){
        //then
        expectedException.expect(ObjectNotFoundException.class);
        expectedException.expectMessage("Person not found");

        entityManager.clear();

        //given
        Person jarek = createJarek();
        long id = personRepository.save(jarek).getId();

        entityManager.clear();

        //when
        personService.deletePerson(id);
        personService.readPerson(id);
    }

    @Test
    public void whenIDeleteAllPersons_AnyPersonDoesNotExist(){
        //given
        Person jarek = createJarek();
        Person adam = createAdam();
        Person adrian = createAdrian();

        personRepository.save(jarek);
        personRepository.save(adam);
        personRepository.save(adrian);

        entityManager.clear();

        //when
        personService.deleteAllPersons();

        entityManager.clear();

        //then
        Assert.assertTrue(personService.readAllPersons().isEmpty());
    }
}
