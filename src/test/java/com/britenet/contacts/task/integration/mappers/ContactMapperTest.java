package com.britenet.contacts.task.integration.mappers;

import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarek;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactMapperTest {

    @Autowired
    private ContactMapper contactMapper;

    @Test
    public void whenIMapAddressToResDTO_IGetContactWithPersonResDTO(){
        //given
        Contact address = createTestAddress();
        address.setId(1L);

        Person person = createJarek();
        person.setId(1L);

        person.addContact(address);

        //when
        ContactWithPersonResDTO contactWithPersonResDTO = contactMapper.mapToResWithPerson(address);

        //then
        assertEquals(1L,contactWithPersonResDTO.getId());
        assertEquals("address",contactWithPersonResDTO.getKind());
        String expectedValue = "ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie";
        assertEquals(expectedValue,contactWithPersonResDTO.getValue());

        PersonResDTO personResDTO = contactWithPersonResDTO.getPerson();
        PersonResDTO expectedPersonResDTO = PersonResDTO.builder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .build();
        assertEquals(personResDTO,expectedPersonResDTO);
    }
}
