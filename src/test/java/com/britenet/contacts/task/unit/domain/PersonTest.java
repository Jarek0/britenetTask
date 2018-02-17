package com.britenet.contacts.task.unit.domain;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.invalidInput.DuplicatedContactException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
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
                "Person with pesel: 99999999998 already have address: ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        expectedException.expectMessage(expectedExceptionMessage);

        //given
        Person person = createAdam();

        Contact address = createTestAddress();

        Contact addressDuplication = createTestAddress();

        //when
        person.addContact(address);
        person.addContact(addressDuplication);
    }

    @Test
    public void whenAddSomeContacts_thenPersonHasThey(){
        //given
        Person person = createAdam();

        Contact address = createTestAddress();

        Contact emailAddress = createTestEmailAddress();

        Contact phoneNumber = createTestPhoneNumber();

        //when
        person.addContact(address);
        person.addContact(emailAddress);
        person.addContact(phoneNumber);

        //then
        assertTrue(person.getContacts().containsAll(Arrays.asList(address,phoneNumber,emailAddress)));
    }
}
