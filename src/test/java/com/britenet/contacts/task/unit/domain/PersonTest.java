package com.britenet.contacts.task.unit.domain;

import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.exceptions.invalidInput.DuplicatedContactException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void tryAddDuplicateContact(){
        //then
        expectedException.expect(DuplicatedContactException.class);
        String expectedExceptionMessage =
                "Person with pesel: 99999999999 already have address: ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        expectedException.expectMessage(expectedExceptionMessage);

        //given
        Person person = Person.builder()
                .name("adam")
                .surname("kowalski")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now())
                .pesel("99999999999")
                .build();

        Address address = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();

        Address addressDuplication = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();

        //when
        person.addContact(address);
        person.addContact(addressDuplication);
    }

    @Test
    public void whenAddSomeContacts_thenPersonHasThey(){
        //given
        Person person = Person.builder()
                .name("adam")
                .surname("kowalski")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now())
                .pesel("99999999999")
                .build();

        Address address = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();

        EmailAddress emailAddress = new EmailAddress("jery0@o2.pl");

        PhoneNumber phoneNumber = new PhoneNumber("999999999");

        //when
        person.addContact(address);
        person.addContact(emailAddress);
        person.addContact(phoneNumber);

        //then
        assertTrue(person.getContacts().containsAll(Arrays.asList(address,phoneNumber,emailAddress)));
    }
}
