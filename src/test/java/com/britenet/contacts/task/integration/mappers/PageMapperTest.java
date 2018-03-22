package com.britenet.contacts.task.integration.mappers;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.page.PageMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumberResDTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageMapperTest {

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private PageMapper pageMapper;

    @Test
    public void whenIMapPage_IGetPageReqDTO(){
        //given
        Contact address = createTestAddress();
        address.setId(1L);

        Contact phoneNumber = createTestPhoneNumber();
        phoneNumber.setId(2L);

        Contact emailAddress = createTestEmailAddress();
        emailAddress.setId(3L);

        //when
        Pageable pageable = new PageRequest(1,3, Sort.Direction.ASC,"kind");
        Page<Contact> page = new PageImpl<>(Arrays.asList(address,phoneNumber,emailAddress),pageable,3);
        PageResDTO<ContactResDTO> pageResDTO = pageMapper.mapToResDTO(page,contactMapper::mapToResDTO);

        //then
        ContactResDTO expectedContact1 = createTestAddressResDTO();
        ContactResDTO expectedContact2 = createTestPhoneNumberResDTO();
        ContactResDTO expectedContact3 = createTestEmailAddressResDTO();

        assertEquals(2,pageResDTO.getTotalPages());
        assertEquals(1,pageResDTO.getCurrentPageNumber());
        assertTrue(pageResDTO.getContent().containsAll(Arrays.asList(expectedContact1,expectedContact2,expectedContact3)));
    }
}
