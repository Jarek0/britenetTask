package com.britenet.contacts.task.unit.mappers;

import com.britenet.contacts.task.DTO.contact.request.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.contact.ContactMapperImpl;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

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
        AddressReqDTO addressReqDTO = AddressReqDTO.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province("małopolskie")
                .flatNumber("2")
                .blockNumber("2a")
                .build();

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
        Contact address = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();
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
        Contact address = Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();
        address.setId(1L);

        Person person = Person.builder()
                .name("Jarek")
                .surname("Bielec")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();
        person.setId(1L);

        person.addContact(address);

        Mockito.when(personMapper.mapToResDTO(person))
                .thenReturn(
                        PersonResDTO.builder()
                        .id(1)
                        .name("Jarek")
                        .surname("Bielec")
                        .gender("male")
                        .birthDate("2016-08-16")
                        .pesel("99999999999")
                        .build()
                );

        //when
        ContactWithPersonResDTO contactWithPersonResDTO = contactMapper.mapToResWithPerson(address);

        //then
        assertEquals(1L,contactWithPersonResDTO.getId());
        assertEquals("address",contactWithPersonResDTO.getKind());
        String expectedValue = "ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        assertEquals(expectedValue,contactWithPersonResDTO.getValue());

        int countOfMappedPersons = 1;
        Mockito.verify(personMapper,times(countOfMappedPersons)).mapToResDTO(captor.capture());
        List<Person> capturedPersons = captor.getAllValues();
        assertEquals(1,capturedPersons.size());
        assertTrue(capturedPersons.containsAll(Collections.singletonList(person)));
    }
}
