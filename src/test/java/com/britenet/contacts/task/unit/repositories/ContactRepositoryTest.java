package com.britenet.contacts.task.unit.repositories;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.contact.ContactRepository;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@DataJpaTest
public class ContactRepositoryTest {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void clearDataBaseBeforeEachTest(){
        contactRepository.deleteAll();
    }

    @Test
    public void whenISaveContactWithPerson_ThenIFindThisContactWithThisPerson(){
        //given
        Person jarek = createJarek();

        Contact jarekAddress = createTestAddress();

        jarek.addContact(jarekAddress);

        //when
        Person savedJarek = personRepository.save(jarek);
        long id = savedJarek.getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //then
        Contact foundContact = contactRepository.findByIdWithPerson(id)
                .orElseThrow(() -> new ObjectNotFoundException(Contact.class,id));
        assertEquals(jarekAddress,foundContact);
        //check n+1 problem
        assertEquals(jarek,foundContact.getPerson());
    }

    @Test
    public void whenISaveSomeContactsWithPersons_ThenIFindAllThisContactsWithTheirPersons(){
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

        //when
        personRepository.save(Arrays.asList(adrian,adam,jarek));

        entityManager.clear();

        //then
        List<Contact> contacts = contactRepository.findAllWithPersons();

        assertTrue(contacts.containsAll(Arrays.asList(jarekAddress,adamAddress,adamPhoneNumber,adrianEmailAddress)));
        assertEquals(4,contacts.size());

        //check n+1 problem
        contacts.forEach(
                c -> {
                    if(c.equals(jarekAddress)){
                        boolean jarekLiveWithAdam = c.getPerson().equals(jarek) || c.getPerson().equals(adam);
                        assertTrue(jarekLiveWithAdam);
                    }
                    else if(c.equals(adamPhoneNumber)){
                        assertEquals(c.getPerson(),adam);
                    }
                    else if(c.equals(adrianEmailAddress)){
                        assertEquals(c.getPerson(),adrian);
                    }
                }
        );
    }

    @Test
    public void whenISaveSomeContactsWithPersons_ThenIFindPageOfContactsWithThisPersons(){
        for(int i=0; i<3; i++){
            personRepository.save(personRepository.save(createNextPersonWithContacts(i)));
        }

        //when
        Pageable pageable = new PageRequest(2,3, Sort.Direction.ASC,"kind");
        Page<Contact> pageOfContacts = contactRepository.findPageWithPersons(pageable);

        entityManager.clear();

        //then
        assertEquals(9,pageOfContacts.getTotalElements());
        assertEquals(3,pageOfContacts.getTotalPages());
        assertEquals(2,pageOfContacts.getNumber());
        assertEquals(3,pageOfContacts.getNumberOfElements());

        Contact expectedContact1 = new PhoneNumber("99999990");
        Contact expectedContact2 = new PhoneNumber("99999992");
        Contact expectedContact3 = new PhoneNumber("99999991");

        assertTrue(pageOfContacts.getContent().containsAll(Arrays.asList(expectedContact1,expectedContact2,expectedContact3)));

        //check n+1 problem
        pageOfContacts.forEach(
                c -> {
                    if(c.equals(expectedContact1)){
                        assertEquals(c.getPerson(),Person.builder().pesel("9999999990").build());
                    }
                    else if(c.equals(expectedContact2)){
                        assertEquals(c.getPerson(),Person.builder().pesel("9999999992").build());
                    }
                    else if(c.equals(expectedContact3)){
                        assertEquals(c.getPerson(),Person.builder().pesel("9999999991").build());
                    }
                }
        );
    }
}
