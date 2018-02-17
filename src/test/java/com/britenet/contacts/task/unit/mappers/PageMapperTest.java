package com.britenet.contacts.task.unit.mappers;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.page.PageMapper;
import com.britenet.contacts.task.mappers.page.PageMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumberResDTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PageMapperTest {

    @Mock
    private ContactMapper contactMapper;

    @Captor
    private ArgumentCaptor<Contact> captor;

    private PageMapper pageMapper = new PageMapperImpl();

    @Test
    public void whenIMapPage_IGetPageReqDTO(){
        //given
        Contact address = createTestAddress();
        address.setId(1L);

        Contact phoneNumber = createTestPhoneNumber();
        phoneNumber.setId(2L);

        Contact emailAddress = createTestEmailAddress();
        emailAddress.setId(3L);

        ContactResDTO mappedContact1 = createTestAddressResDTO();

        Mockito.when(contactMapper.mapToResDTO(address))
                .thenReturn(mappedContact1);

        ContactResDTO mappedContact2 = createTestPhoneNumberResDTO();

        Mockito.when(contactMapper.mapToResDTO(phoneNumber))
                .thenReturn(mappedContact2);

        ContactResDTO mappedContact3 = createTestEmailAddressResDTO();

        Mockito.when(contactMapper.mapToResDTO(emailAddress))
                .thenReturn(mappedContact3);

        //when
        Pageable pageable = new PageRequest(1,3, Sort.Direction.ASC,"kind");
        Page<Contact> page = new PageImpl<>(Arrays.asList(address,phoneNumber,emailAddress),pageable,3);
        PageResDTO<ContactResDTO> pageResDTO = pageMapper.mapToResDTO(page,contactMapper::mapToResDTO);

        //then
        int countOfMappedContacts = 3;
        Mockito.verify(contactMapper,times(countOfMappedContacts)).mapToResDTO(captor.capture());
        List<Contact> capturedContacts = captor.getAllValues();
        assertEquals(3,capturedContacts.size());
        assertTrue(capturedContacts.containsAll(Arrays.asList(address,phoneNumber,emailAddress)));

        assertEquals(2,pageResDTO.getTotalPages());
        assertEquals(1,pageResDTO.getCurrentPageNumber());
        assertTrue(pageResDTO.getContent().containsAll(Arrays.asList(mappedContact1,mappedContact2,mappedContact3)));
    }
}
