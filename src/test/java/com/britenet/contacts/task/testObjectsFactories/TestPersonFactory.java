package com.britenet.contacts.task.testObjectsFactories;

import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;

import java.time.LocalDate;

public class TestPersonFactory {

    public static Person createJarek(){
        return Person.builder()
                .name("Jarek")
                .surname("Bielec")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();
    }

    public static Person createAdam(){
        return Person.builder()
                .name("Adam")
                .surname("Kowalski")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999998")
                .build();
    }

    public static Person createAdrian(){
        return Person.builder()
                .name("Adrian")
                .surname("Nowak")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2015-08-16"))
                .pesel("99999999997")
                .build();
    }

    public static PersonReqDTO createJarekReqDTO(){
        return PersonReqDTO.builder()
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();
    }

    public static Person createNextPersonWithContacts(int iterationNumber){
        Person person = Person.builder()
                .name("Person")
                .surname("Person")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-15"))
                .pesel("999999999"+iterationNumber)
                .build();

        Contact address = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2"+iterationNumber)
                .build();

        Contact phoneNumber = new PhoneNumber("9999999"+iterationNumber);

        Contact emailAddress = new EmailAddress("jery"+iterationNumber+"@o2.pl");

        person.addAllContacts(address,phoneNumber,emailAddress);

        return person;
    }
}
