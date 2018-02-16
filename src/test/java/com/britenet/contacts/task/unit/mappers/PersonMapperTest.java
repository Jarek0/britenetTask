package com.britenet.contacts.task.unit.mappers;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import com.britenet.contacts.task.mappers.person.PersonMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PersonMapperTest {

    @Mock
    private ContactMapper contactMapper;

    @Captor
    private ArgumentCaptor<Contact> captor;

    private PersonMapper personMapper;

    @Before
    public void init() {
        personMapper = new PersonMapperImpl(contactMapper);
    }

    @Test
    public void whenIMapFromReqDTO_IGetPerson(){
        //given
        PersonReqDTO personReqDTO = PersonReqDTO.builder()
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();

        //when
        Person person = personMapper.mapFromReqDTO(personReqDTO);

        //then
        assertNull(person.getId());
        assertEquals("Jarek",person.getName());
        assertEquals("Bielec",person.getSurname());
        assertEquals(Gender.MALE,person.getGender());
        assertEquals(LocalDate.parse("2016-08-16"),person.getBirthDate());
        assertEquals("99999999999",person.getPesel());
        assertTrue(person.getContacts().isEmpty());
    }

    @Test
    public void whenIMapPersonToResDTO_IGetPersonResDTO(){
        //given
        Person person = Person.builder()
                .name("Jarek")
                .surname("Bielec")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();
        person.setId(1L);

        //when
        PersonResDTO personResDTO = personMapper.mapToResDTO(person);

        //then
        assertEquals(1L,personResDTO.getId());
        assertEquals("Jarek",personResDTO.getName());
        assertEquals("Bielec",personResDTO.getSurname());
        assertEquals("male",personResDTO.getGender());
        assertEquals("2016-08-16",personResDTO.getBirthDate());
        assertEquals("99999999999",personResDTO.getPesel());
    }

    @Test
    public void whenIMapPersonToResDTO_IGetPersonResDTOWithContacts(){
        //given
        Person person = Person.builder()
                .name("Jarek")
                .surname("Bielec")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();
        person.setId(1L);

        Contact address = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();
        address.setId(1L);

        Contact phoneNumber = new PhoneNumber("999999999");
        phoneNumber.setId(2L);

        Contact emailAddress = new EmailAddress("jery0@o2.pl");
        emailAddress.setId(3L);

        person.addContact(address);
        person.addContact(phoneNumber);
        person.addContact(emailAddress);

        String addressValue = "ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        Mockito.when(contactMapper.mapToResDTO(address))
                .thenReturn(
                        ContactResDTO.builder()
                                .id(1L)
                                .kind("address")
                                .value(addressValue)
                                .build()
                );
        Mockito.when(contactMapper.mapToResDTO(phoneNumber))
                .thenReturn(
                        ContactResDTO.builder()
                                .id(2L)
                                .kind("phone number")
                                .value("999999999")
                                .build()
                );
        Mockito.when(contactMapper.mapToResDTO(emailAddress))
                .thenReturn(
                        ContactResDTO.builder()
                                .id(3L)
                                .kind("jery0@o2.pl")
                                .value(addressValue)
                                .build()
                );

        //when
        PersonWithContactsResDTO personWithContactsResDTO = personMapper.mapToResWithContactsDTO(person);

        //then
        assertEquals(1L,personWithContactsResDTO.getId());
        assertEquals("Jarek",personWithContactsResDTO.getName());
        assertEquals("Bielec",personWithContactsResDTO.getSurname());
        assertEquals("male",personWithContactsResDTO.getGender());
        assertEquals("2016-08-16",personWithContactsResDTO.getBirthDate());
        assertEquals("99999999999",personWithContactsResDTO.getPesel());

        int countOfMappedContacts = 3;
        Mockito.verify(contactMapper,times(countOfMappedContacts)).mapToResDTO(captor.capture());
        List<Contact> capturedContacts = captor.getAllValues();
        assertEquals(3,capturedContacts.size());
        assertTrue(capturedContacts.containsAll(Arrays.asList(address,phoneNumber,emailAddress)));
    }
}
