package com.britenet.contacts.task.integration.repositories;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.contact.PhoneNumberRepository;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarek;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@DataJpaTest
public class PhoneNumberRepositoryTest {
    @Autowired
    PhoneNumberRepository phoneNumberRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void clearDataBaseBeforeEachTest(){
        phoneNumberRepository.deleteAll();
    }

    @Test
    public void whenISaveEmailAddressWithPerson_thenIFindThisEmailAddressWithThisPerson(){
        //given
        Person jarek = createJarek();

        PhoneNumber jarekPhoneNumber = (PhoneNumber) createTestPhoneNumber();

        jarek.addContact(jarekPhoneNumber);

        //when
        Person savedJarek = personRepository.save(jarek);
        long id = savedJarek.getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //then
        Contact foundContact = phoneNumberRepository.findPhoneNumberByIdWithPerson(id)
                .orElseThrow(() -> new ObjectNotFoundException(Contact.class,id));
        assertEquals(jarekPhoneNumber,foundContact);
        //check n+1 problem
        assertEquals(jarek,foundContact.getPerson());
    }
}
