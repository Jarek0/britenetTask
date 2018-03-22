package com.britenet.contacts.task.testObjectsFactories;

import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestContactFactory {

    public static List<ContactWithPersonResDTO> createPageResDTOContent(){
        PersonResDTO expectedPersonResDTO1 = PersonResDTO.builder()
                .id(1L)
                .name("Person")
                .surname("Person")
                .gender("male")
                .birthDate("2016-08-15")
                .pesel("9999999990")
                .build();

        ContactWithPersonResDTO expectedContactResDTO1 = ContactWithPersonResDTO.subBuilder()
                .id(1L)
                .person(expectedPersonResDTO1)
                .kind("phone number")
                .value("99999990")
                .build();

        PersonResDTO expectedPersonResDTO2 = PersonResDTO.builder()
                .id(2L)
                .name("Person")
                .surname("Person")
                .gender("male")
                .birthDate("2016-08-15")
                .pesel("9999999992")
                .build();

        ContactWithPersonResDTO expectedContactResDTO2 = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .person(expectedPersonResDTO2)
                .kind("phone number")
                .value("99999992")
                .build();

        PersonResDTO expectedPersonResDTO3 = PersonResDTO.builder()
                .id(3L)
                .name("Person")
                .surname("Person")
                .gender("male")
                .birthDate("2016-08-15")
                .pesel("9999999991")
                .build();

        ContactWithPersonResDTO expectedContactResDTO3 = ContactWithPersonResDTO.subBuilder()
                .id(3L)
                .person(expectedPersonResDTO3)
                .kind("phone number")
                .value("99999991")
                .build();

        return Arrays.asList(expectedContactResDTO1,expectedContactResDTO2,expectedContactResDTO3);
    }

    public static List<Contact> createPageContact(){
        Person person1 = Person.builder()
                .name("Person")
                .surname("Person")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-15"))
                .pesel("9999999990")
                .build();

        Contact contact1 = new PhoneNumber("99999990");

        person1.addContact(contact1);

        Person person2 = Person.builder()
                .name("Person")
                .surname("Person")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-15"))
                .pesel("9999999992")
                .build();

        Contact contact2 = new PhoneNumber("99999992");

        person1.addContact(contact2);

        Person person3 = Person.builder()
                .name("Person")
                .surname("Person")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-15"))
                .pesel("9999999991")
                .build();


        Contact contact3 = new PhoneNumber("99999991");

        person3.addContact(contact3);

        return Arrays.asList(contact1,contact2,contact3);
    }
}
