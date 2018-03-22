package com.britenet.contacts.task.integration.repositories;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.contact.AddressRepository;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarek;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void clearDataBaseBeforeEachTest(){
        addressRepository.deleteAll();
    }

    @Test
    public void whenISaveAddressWithPerson_thenIFindThisAddressWithThisPerson(){
        //given
        Person jarek = createJarek();

        Address jarekAddress = (Address) createTestAddress();

        jarek.addContact(jarekAddress);

        //when
        Person savedJarek = personRepository.save(jarek);
        long id = savedJarek.getContacts().stream().findFirst().get().getId();

        entityManager.clear();

        //then
        Contact foundContact = addressRepository.findAddressByIdWithPerson(id)
                .orElseThrow(() -> new ObjectNotFoundException(Contact.class,id));
        assertEquals(jarekAddress,foundContact);
        //check n+1 problem
        assertEquals(jarek,foundContact.getPerson());
    }
}
