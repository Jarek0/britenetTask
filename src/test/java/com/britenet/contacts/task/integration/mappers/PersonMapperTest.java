package com.britenet.contacts.task.integration.mappers;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarek;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonMapperTest {

    @Autowired
    PersonMapper personMapper;

    @Test
    public void whenIMapPersonToResDTO_IGetPersonResDTOWithContacts(){
        //given
        Person person = createJarek();
        person.setId(1L);

        Contact address = createTestAddress();
        address.setId(1L);

        Contact phoneNumber = createTestPhoneNumber();
        phoneNumber.setId(2L);

        Contact emailAddress = createTestEmailAddress();
        emailAddress.setId(3L);

        person.addAllContacts(address,phoneNumber,emailAddress);

        //when
        PersonWithContactsResDTO personWithContactsResDTO = personMapper.mapToResWithContactsDTO(person);

        //then
        PersonWithContactsResDTO expectedPersonWithContactsResDTO = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .build();

        assertEquals(expectedPersonWithContactsResDTO,personWithContactsResDTO);

        ContactResDTO expectedAddressResDTO = ContactResDTO.builder()
                .id(1L)
                .kind("address")
                .value("ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie")
                .build();

        ContactResDTO expectedPhoneNumberResDTO = ContactResDTO.builder()
                .id(2L)
                .kind("phone number")
                .value("999999999")
                .build();

        ContactResDTO expectedEmailAddressResDTO = ContactResDTO.builder()
                .id(3L)
                .kind("e-mail address")
                .value("jery0@o2.pl")
                .build();

        List<ContactResDTO> expectedContacts = Arrays.asList(expectedAddressResDTO,expectedPhoneNumberResDTO,expectedEmailAddressResDTO);

        assertEquals(3,personWithContactsResDTO.getContacts().size());
        assertTrue(personWithContactsResDTO.getContacts().contains(expectedAddressResDTO));
    }
}
