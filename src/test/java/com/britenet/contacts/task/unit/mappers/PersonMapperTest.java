package com.britenet.contacts.task.unit.mappers;

import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
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

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumberResDTO;
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
        PersonReqDTO personReqDTO = createJarekReqDTO();

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
        Person person = createJarek();
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
        Person person = createJarek();
        person.setId(1L);

        Contact address = createTestAddress();
        address.setId(1L);

        Contact phoneNumber = createTestPhoneNumber();
        phoneNumber.setId(2L);

        Contact emailAddress = createTestEmailAddress();
        emailAddress.setId(3L);

        person.addAllContacts(address,phoneNumber,emailAddress);

        Mockito.when(contactMapper.mapToResDTO(address))
                .thenReturn(createTestAddressResDTO());
        Mockito.when(contactMapper.mapToResDTO(phoneNumber))
                .thenReturn(createTestPhoneNumberResDTO());
        Mockito.when(contactMapper.mapToResDTO(emailAddress))
                .thenReturn(createTestEmailAddressResDTO());

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
