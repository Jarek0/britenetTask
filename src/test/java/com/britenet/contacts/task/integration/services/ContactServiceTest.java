package com.britenet.contacts.task.integration.services;

import com.britenet.contacts.task.DTO.contact.request.update.UpdateAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdateEmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdatePhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import com.britenet.contacts.task.services.contact.ContactService;
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

import java.util.Arrays;
import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestContactFactory.createPageResDTOContent;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
public class ContactServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactService contactService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void clearDataBaseBeforeEachTest(){
        personRepository.deleteAll();
    }

    @Test
    public void whenIReadSavedContactWithPerson_thenIGetContactWithPersonDTO(){
        //given
        Person jarek = createJarek();

        Contact jarekAddress = createTestAddress();

        jarek.addContact(jarekAddress);

        Person savedJarek = personRepository.save(jarek);
        long id = savedJarek.getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //when
        ContactWithPersonResDTO contactWithPersonResDTO = contactService.readContact(id);

        //then
        PersonResDTO expectedPersonResDTO = PersonResDTO.builder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .build();

        assertEquals(expectedPersonResDTO,contactWithPersonResDTO.getPerson());

        ContactWithPersonResDTO expectedContactWithPersonResDTO = ContactWithPersonResDTO.subBuilder()
                .id(1L)
                .kind("address")
                .value("ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie")
                .person(expectedPersonResDTO)
                .build();

        assertEquals(expectedContactWithPersonResDTO,contactWithPersonResDTO);
    }

    @Test
    public void whenIReadNotExistingContact_thenIGetObjectNotFoundException(){
        //then
        expectedException.expect(ObjectNotFoundException.class);
        expectedException.expectMessage("Contact not found");

        //when
        ContactWithPersonResDTO contactWithPersonResDTO = contactService.readContact(1L);
    }

    @Test
    public void whenIReadSomeSavedContactsWithPersons_thenIGetContactWithPersonDTOs(){
        //given
        Person jarek = createJarek();
        Contact jarekAddress = createTestAddress();
        jarek.addContact(jarekAddress);

        Person adam = createAdam();
        Contact adamAddress = createTestAddress();
        Contact adamPhoneNumber = createTestPhoneNumber();
        adam.addAllContacts(adamPhoneNumber,adamAddress);

        Person adrian = createAdrian();
        Contact adrianEmailAddress = createTestEmailAddress();
        adrian.addContact(adrianEmailAddress);

        personRepository.save(Arrays.asList(adrian,adam,jarek));

        entityManager.clear();

        //when
        List<ContactWithPersonResDTO> contactWithPersonResDTOS = contactService.readAllContacts();

        //then
        PersonResDTO expectedPersonResDTO1 = createJarekResDTO();
        PersonResDTO expectedPersonResDTO2 = createAdamResDTO();
        PersonResDTO expectedPersonResDTO3 = createAdrianResDTO();

        ContactWithPersonResDTO expectedContactResDTO1 = ContactWithPersonResDTO.subBuilder()
                .id(1L)
                .kind("address")
                .value("ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie")
                .person(expectedPersonResDTO1)
                .build();

        ContactWithPersonResDTO expectedContactResDTO2 = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("address")
                .value("ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie")
                .person(expectedPersonResDTO2)
                .build();

        ContactWithPersonResDTO expectedContactResDTO3 = ContactWithPersonResDTO.subBuilder()
                .id(3L)
                .kind("phone number")
                .value("999999999")
                .person(expectedPersonResDTO2)
                .build();

        ContactWithPersonResDTO expectedContactResDTO4 = ContactWithPersonResDTO.subBuilder()
                .id(4L)
                .kind("e-mail address")
                .value("jery0@o2.pl")
                .person(expectedPersonResDTO3)
                .build();

        List<ContactWithPersonResDTO> expectedContactResDTOs =
                Arrays.asList(expectedContactResDTO1,expectedContactResDTO2,expectedContactResDTO3,expectedContactResDTO4);

        assertEquals(4,contactWithPersonResDTOS.size());
        assertTrue(contactWithPersonResDTOS.containsAll(expectedContactResDTOs));
    }

    @Test
    public void whenIReadSomeSavedContactsWithPersons_thenIGetPageOfContactWithPersonDTOs(){
        //given
        for(int i=0; i<3; i++){
            personRepository.save(personRepository.save(createNextPersonWithContacts(i)));
        }

        //when
        PageResDTO<ContactWithPersonResDTO> pageOfContactsResDTOs =
                contactService.readPageOfContacts(2,3,"kind","ASC");

        //then
        assertEquals(3,pageOfContactsResDTOs.getTotalPages());
        assertEquals(2,pageOfContactsResDTOs.getCurrentPageNumber());


        List<ContactWithPersonResDTO> expectedResDTOs = createPageResDTOContent();

        assertEquals(3,pageOfContactsResDTOs.getContent().size());
        assertTrue(pageOfContactsResDTOs.getContent().containsAll(expectedResDTOs));
    }

    @Test
    public void whenIUpdateSavedAddress_thenIGetContactResDTOWithUpdatedData(){
        //given
        Address addressToUpdate = (Address) createTestAddress();
        Person jarek = createJarek();
        jarek.addContact(addressToUpdate);
        long id = personRepository.save(jarek).getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //when
        UpdateAddressReqDTO updateReqDTO = UpdateAddressReqDTO.subBuilder()
                .id(id)
                .town("Zamość")
                .zipCode("22-410")
                .street("ul. 3 Maja")
                .province("lubelskie")
                .flatNumber("3")
                .build();
        ContactWithPersonResDTO contactWithPersonResDTO = contactService.updateContact(updateReqDTO);

        //then
        PersonResDTO expectedPersonResDTO = createJarekResDTO();
        ContactWithPersonResDTO expectedContactResDTO = ContactWithPersonResDTO.subBuilder()
                .id(id)
                .kind("address")
                .person(expectedPersonResDTO)
                .value("ul. 3 Maja 3 22-410 Zamość woj. lubelskie")
                .build();
        assertEquals(expectedContactResDTO,contactWithPersonResDTO);
    }

    @Test
    public void whenIUpdateSavedEmailAddress_thenIGetContactResDTOWithUpdatedData(){
        //given
        EmailAddress emailAddress = (EmailAddress) createTestEmailAddress();
        Person jarek = createJarek();
        jarek.addContact(emailAddress);
        long id = personRepository.save(jarek).getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //when
        UpdateEmailAddressReqDTO updateReqDTO = new UpdateEmailAddressReqDTO("jarek0@o2.pl",id);
        ContactWithPersonResDTO contactWithPersonResDTO = contactService.updateContact(updateReqDTO);

        //then
        PersonResDTO expectedPersonResDTO = createJarekResDTO();
        ContactWithPersonResDTO expectedContactResDTO = ContactWithPersonResDTO.subBuilder()
                .id(id)
                .kind("e-mail address")
                .person(expectedPersonResDTO)
                .value("jarek0@o2.pl")
                .build();
        assertEquals(expectedContactResDTO,contactWithPersonResDTO);
    }

    @Test
    public void whenIUpdateSavedPhoneNumber_thenIGetContactResDTOWithUpdatedData(){
        //given
        PhoneNumber phoneNumber = (PhoneNumber) createTestPhoneNumber();
        Person jarek = createJarek();
        jarek.addContact(phoneNumber);
        long id = personRepository.save(jarek).getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //when
        UpdatePhoneNumberReqDTO updateReqDTO = new UpdatePhoneNumberReqDTO("111111111",id);
        ContactWithPersonResDTO contactWithPersonResDTO = contactService.updateContact(updateReqDTO);

        //then
        PersonResDTO expectedPersonResDTO = createJarekResDTO();
        ContactWithPersonResDTO expectedContactResDTO = ContactWithPersonResDTO.subBuilder()
                .id(id)
                .kind("phone number")
                .person(expectedPersonResDTO)
                .value("111111111")
                .build();
        assertEquals(expectedContactResDTO,contactWithPersonResDTO);
    }

    @Test
    public void whenIDeleteSavedContactAndTryToFindIt_thenIGetException(){
        //then
        expectedException.expect(ObjectNotFoundException.class);
        expectedException.expectMessage("Contact not found");

        entityManager.clear();

        //given
        PhoneNumber phoneNumber = (PhoneNumber) createTestPhoneNumber();
        Person jarek = createJarek();
        jarek.addContact(phoneNumber);
        long id = personRepository.save(jarek).getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //when
        contactService.deleteContact(id);
        contactService.readContact(id);
    }

    @Test
    public void whenIDeleteAllContacts_AnyContactDoesNotExist(){
        //given
        Person jarek = createJarek();
        Contact jarekAddress = createTestAddress();
        Contact jarekPhoneNumber = createTestPhoneNumber();
        Contact jarekEmailAddress = createTestEmailAddress();
        jarek.addAllContacts(jarekAddress,jarekPhoneNumber,jarekEmailAddress);
        personRepository.save(jarek);

        entityManager.clear();

        //when
        contactService.deleteAllContacts();

        entityManager.clear();

        //then
        assertTrue(contactService.readAllContacts().isEmpty());
    }
}
