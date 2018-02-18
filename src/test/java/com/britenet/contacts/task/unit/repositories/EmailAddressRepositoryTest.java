package com.britenet.contacts.task.unit.repositories;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.contact.EmailAddressRepository;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarek;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@DataJpaTest
public class EmailAddressRepositoryTest {

    @Autowired
    EmailAddressRepository emailAddressRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void clearDataBaseBeforeEachTest(){
        emailAddressRepository.deleteAll();
    }

    @Test
    public void whenISaveEmailAddressWithPerson_thenIFindThisEmailAddressWithThisPerson(){
        //given
        Person jarek = createJarek();

        EmailAddress jarekEmailAddress = (EmailAddress) createTestEmailAddress();

        jarek.addContact(jarekEmailAddress);

        //when
        Person savedJarek = personRepository.save(jarek);
        long id = savedJarek.getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //then
        Contact foundContact = emailAddressRepository.findEmailAddressByIdWithPerson(id)
                .orElseThrow(() -> new ObjectNotFoundException(Contact.class,id));
        assertEquals(jarekEmailAddress,foundContact);
        //check n+1 problem
        assertEquals(jarek,foundContact.getPerson());
    }
}
