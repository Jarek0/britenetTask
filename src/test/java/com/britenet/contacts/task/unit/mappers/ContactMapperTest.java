package com.britenet.contacts.task.unit.mappers;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.contact.ContactMapperImpl;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressReqDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarek;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ContactMapperTest {

    @Mock
    private PersonMapper personMapper;

    @Captor
    private ArgumentCaptor<Person> captor;

    private ContactMapper contactMapper;

    @Before
    public void init(){
        contactMapper = new ContactMapperImpl(personMapper);
    }

    @Test
    public void whenIMapAddressReqDTO_IGetAddress(){
        //given
        AddressReqDTO addressReqDTO = createTestAddressReqDTO();

        //when
        Address address = (Address) contactMapper.fromReqDTO(addressReqDTO);

        //then
        assertNull(address.getId());
        assertEquals("address",address.getKind());
        assertNull(address.getPerson());
        assertEquals("Lublin",address.getTown());
        assertEquals("22-420",address.getZipCode());
        assertEquals("ul. Tomasza Zana",address.getStreet());
        assertEquals(Province.malopolskie,address.getProvince());
        assertEquals("2",address.getFlatNumber());
        assertEquals("2a",address.getBlockNumber());
    }

    @Test
    public void whenIMapPhoneNumberReqDTO_IGetPhoneNumber(){
        //given
        PhoneNumberReqDTO phoneNumberReqDTO = new PhoneNumberReqDTO("999999999");

        //when
        PhoneNumber phoneNumber = (PhoneNumber) contactMapper.fromReqDTO(phoneNumberReqDTO);

        //then
        assertNull(phoneNumber.getId());
        assertEquals("phone number",phoneNumber.getKind());
        assertNull(phoneNumber.getPerson());
        assertEquals("999999999",phoneNumber.getValue());
    }

    @Test
    public void whenIMapEmailAddressReqDTO_IGetEmailAddress(){
        //given
        EmailAddressReqDTO emailAddressReqDTO = new EmailAddressReqDTO("jery0@o2.pl");

        //when
        EmailAddress emailAddress = (EmailAddress) contactMapper.fromReqDTO(emailAddressReqDTO);

        //then
        assertNull(emailAddress.getId());
        assertEquals("e-mail address",emailAddress.getKind());
        assertNull(emailAddress.getPerson());
        assertEquals("jery0@o2.pl",emailAddress.getValue());
    }

    @Test
    public void whenIMapAddressToResDTO_IGetContactResDTO(){
        //given
        Contact address = createTestAddress();
        address.setId(1L);

        //when
        ContactResDTO contactResDTO = contactMapper.mapToResDTO(address);

        //then
        assertEquals(1L,contactResDTO.getId());
        assertEquals("address",contactResDTO.getKind());
        String expectedValue = "ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        assertEquals(expectedValue,contactResDTO.getValue());
    }

    @Test
    public void whenIMapAddressToResDTO_IGetContactWithPersonResDTO(){
        //given
        Contact address = createTestAddress();
        address.setId(1L);

        Person person = createJarek();
        person.setId(1L);

        person.addContact(address);

        when(personMapper.mapToResDTO(person)).thenReturn(any(PersonResDTO.class));

        //when
        ContactWithPersonResDTO contactWithPersonResDTO = contactMapper.mapToResWithPerson(address);

        //then
        assertEquals(1L,contactWithPersonResDTO.getId());
        assertEquals("address",contactWithPersonResDTO.getKind());
        String expectedValue = "ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        assertEquals(expectedValue,contactWithPersonResDTO.getValue());

        int countOfMappedPersons = 1;
        verify(personMapper,times(countOfMappedPersons)).mapToResDTO(captor.capture());
        List<Person> capturedPersons = captor.getAllValues();
        assertEquals(1,capturedPersons.size());
        assertTrue(capturedPersons.containsAll(Collections.singletonList(person)));
    }
}
