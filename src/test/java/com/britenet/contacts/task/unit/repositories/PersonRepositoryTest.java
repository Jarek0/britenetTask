package com.britenet.contacts.task.unit.repositories;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.repositories.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress2;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress2;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber2;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void clearDataBaseBeforeEachTest(){
        personRepository.deleteAll();
    }

    @Test
    public void whenISavePerson_ThenIFindItById(){
        //given
        Person jarek = createJarek();

        //when
        long id = personRepository.save(jarek).getId();

        entityManager.clear();

        //then
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,id));

        assertEquals(jarek,foundPerson);
    }

    @Test
    public void whenISavePerson_ThenIFindItByPesel(){
        //given
        Person jarek = createJarek();

        //when
        personRepository.save(jarek);

        entityManager.clear();

        //then
        Person foundPerson = personRepository.findByPesel("99999999999")
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,"pesel","99999999999"));
        assertEquals(jarek,foundPerson);
    }

    @Test
    public void whenISavePersonWithContacts_ThenIFindItByIdWithContacts(){
        //given
        Person jarek = createJarek();

        Contact jarekAddress = createTestAddress();

        Contact jarekPhoneNumber = createTestPhoneNumber();

        Contact jarekEmailAddress = createTestEmailAddress();

        jarek.addAllContacts(jarekAddress,jarekPhoneNumber,jarekEmailAddress);

        //when
        long id = personRepository.save(jarek).getId();

        entityManager.clear();

        //then
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,id));
        assertEquals(jarek,foundPerson);

        //check n+1 problem
        Set<Contact> jarekContacts = jarek.getContacts();
        assertTrue(jarekContacts.containsAll(Arrays.asList(jarekAddress,jarekEmailAddress,jarekPhoneNumber)));
        assertEquals(3,jarekContacts.size());
    }

    @Test
    public void whenISavePersonWithContacts_ThenIFindItByPeselWithContacts(){
        //given
        Person jarek = createJarek();

        Contact jarekAddress = createTestAddress();

        Contact jarekPhoneNumber = createTestPhoneNumber();

        Contact jarekEmailAddress = createTestEmailAddress();

        jarek.addAllContacts(jarekAddress,jarekPhoneNumber,jarekEmailAddress);

        //when
        personRepository.save(jarek);

        entityManager.clear();

        //then
        Person foundPerson = personRepository.findByPesel("99999999999")
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,"pesel","99999999999"));
        assertEquals(jarek,foundPerson);

        //check n+1 problem
        Set<Contact> jarekContacts = jarek.getContacts();
        assertTrue(jarekContacts.containsAll(Arrays.asList(jarekAddress,jarekEmailAddress,jarekPhoneNumber)));
        assertEquals(3,jarekContacts.size());
    }

    @Test
    public void whenISaveSomePersonsWithContacts_ThenIFindAllThisPersonsWithTheirContacts(){
        //given
        Person jarek = createJarek();

        Person adam = createAdam();

        Contact jarekAddress = createTestAddress();

        Contact jarekPhoneNumber = createTestPhoneNumber();

        Contact jarekEmailAddress = createTestEmailAddress();

        Contact adamAddress = createTestAddress2();

        Contact adamPhoneNumber = createTestPhoneNumber2();

        Contact adamEmailAddress = createTestEmailAddress2();

        jarek.addAllContacts(jarekAddress,jarekPhoneNumber,jarekEmailAddress);
        adam.addAllContacts(adamAddress,adamPhoneNumber,adamEmailAddress);

        //when
        personRepository.save(jarek);
        personRepository.save(adam);

        entityManager.clear();

        //then
        List<Person> people = personRepository.findAllWithContacts();
        assertTrue(people.containsAll(Arrays.asList(jarek,adam)));
        assertEquals(2,people.size());

        //check n+1 problem
        Set<Contact> jarekContacts = people.stream()
                .filter(p -> p.getName().equals("Jarek"))
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,"name","Jarek"))
                .getContacts();
        Set<Contact> adamContacts = people.stream()
                .filter(p -> p.getName().equals("Adam"))
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException(Person.class,"name","Adam"))
                .getContacts();

        assertTrue(jarekContacts.containsAll(Arrays.asList(jarekAddress,jarekEmailAddress,jarekPhoneNumber)));
        assertEquals(3,jarekContacts.size());
        assertTrue(adamContacts.containsAll(Arrays.asList(adamAddress,adamEmailAddress,adamPhoneNumber)));
        assertEquals(3,adamContacts.size());
    }

    @Test
    public void whenISaveSomePersonsWithContacts_ThenIFindPageOfPersonsWithTheirContacts(){
        //given
        for(int i=0;i<11;i++){
            personRepository.save(createNextPersonWithContacts(i));
        }

        //when
        Pageable pageable = new PageRequest(3,3, Sort.Direction.ASC,"pesel");
        Page<Person> pageOfPeople = personRepository.findPageWithContacts(pageable);

        entityManager.clear();

        //then
        assertEquals(11,pageOfPeople.getTotalElements());
        assertEquals(4,pageOfPeople.getTotalPages());
        assertEquals(3,pageOfPeople.getNumber());
        assertEquals(2,pageOfPeople.getNumberOfElements());

        Person expectedPerson1 = Person.builder()
                .pesel("9999999998")
                .build();

        Person expectedPerson2 = Person.builder()
                .pesel("9999999999")
                .build();

        assertEquals(expectedPerson1,pageOfPeople.getContent().get(0));
        assertEquals(expectedPerson2,pageOfPeople.getContent().get(1));

        //check n+1 problem

        Contact expectedAddress1 = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("28")
                .build();

        Contact expectedPhoneNumber1 = new PhoneNumber("99999998");

        Contact expectedEmailAddress1 = new EmailAddress("jery8@o2.pl");

        Contact expectedAddress2 = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("29")
                .build();

        Contact expectedPhoneNumber2 = new PhoneNumber("99999999");

        Contact expectedEmailAddress2 = new EmailAddress("jery9@o2.pl");

        assertEquals(
                Stream.of(expectedAddress1,expectedPhoneNumber1,expectedEmailAddress1).collect(Collectors.toSet()),
                pageOfPeople.getContent().get(0).getContacts()
        );
        assertEquals(
                Stream.of(expectedAddress2,expectedPhoneNumber2,expectedEmailAddress2).collect(Collectors.toSet()),
                pageOfPeople.getContent().get(1).getContacts()
        );
    }
}
