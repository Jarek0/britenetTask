package com.britenet.contacts.task.unit.services;

import com.britenet.contacts.task.DTO.contact.request.update.UpdateAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdateEmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdatePhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.domain.person.Person;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.page.PageMapper;
import com.britenet.contacts.task.repositories.contact.AddressRepository;
import com.britenet.contacts.task.repositories.contact.ContactRepository;
import com.britenet.contacts.task.repositories.contact.EmailAddressRepository;
import com.britenet.contacts.task.repositories.contact.PhoneNumberRepository;
import com.britenet.contacts.task.services.contact.ContactService;
import com.britenet.contacts.task.services.contact.ContactServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestContactFactory.createPageContact;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddress;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumber;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ContactServiceTest {

    @Mock
    private EmailAddressRepository emailAddressRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PhoneNumberRepository phoneNumberRepository;
    @Mock
    private PageMapper pageMapper;
    @Mock
    private ContactMapper contactMapper;

    private ContactService contactService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Captor
    private ArgumentCaptor<Long> idCaptor;
    @Captor
    private ArgumentCaptor<Contact> contactArgumentCaptor;
    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;
    @Captor
    private ArgumentCaptor<EmailAddress> emailAddressArgumentCaptor;
    @Captor
    private ArgumentCaptor<PhoneNumber> phoneNumberArgumentCaptor;

    @Before
    public void init(){
        contactService = new ContactServiceImpl(
                contactRepository,
                addressRepository,
                emailAddressRepository,
                phoneNumberRepository,
                pageMapper,
                contactMapper
        );
    }

    @Test
    public void whenIReadSavedContactWithPerson_thenItIsReadAndMapped(){

        //given
        Contact foundContact = createTestAddress();
        Person jarek = createJarek();
        jarek.addContact(foundContact);

        when(contactRepository.findByIdWithPerson(1L)).thenReturn(Optional.ofNullable(foundContact));
        when(contactMapper.mapToResWithPerson(foundContact)).thenReturn(any(ContactWithPersonResDTO.class));

        //when
        contactService.readContact(1L);

        //then
        verify(contactRepository, times(1)).findByIdWithPerson(idCaptor.capture());
        assertEquals(new Long(1L),idCaptor.getValue());
        verify(contactMapper, times(1)).mapToResWithPerson(contactArgumentCaptor.capture());
        assertEquals(foundContact,contactArgumentCaptor.getValue());
    }

    @Test
    public void whenIReadNotExistingContact_thenIGetObjectNotFoundException(){
        //then
        expectedException.expect(ObjectNotFoundException.class);
        expectedException.expectMessage("Contact not found");

        //given
        when(contactRepository.findByIdWithPerson(1L)).thenReturn(Optional.empty());
        when(contactMapper.mapToResWithPerson(any(Contact.class))).thenReturn(any(ContactWithPersonResDTO.class));

        //then
        contactService.readContact(1L);
    }

    @Test
    public void whenIReadSomeSavedContactsWithPersons_thenTheyAreReadAndMapped(){
        //given
        Person jarek = createJarek();
        Contact jarekAddress = createTestAddress();
        jarek.addContact(jarekAddress);

        Person adam = createAdam();
        Contact adamAddress = createTestAddress();
        Contact adamPhoneNumber = createTestPhoneNumber();
        adam.addAllContacts(adamPhoneNumber,adamAddress);

        Person adrian = createAdrian();
        Contact adrianEmailAddress = createTestEmailAddress();
        adrian.addContact(adrianEmailAddress);

        List<Contact> contacts = Arrays.asList(jarekAddress,adamAddress,adamPhoneNumber,adrianEmailAddress);

        when(contactRepository.findAllWithPersons()).thenReturn(contacts);

        //when
        contactService.readAllContacts();

        //then
        verify(contactRepository, times(1)).findAllWithPersons();
        verify(contactMapper, times(4)).mapToResWithPerson(contactArgumentCaptor.capture());
        assertEquals(contacts,contactArgumentCaptor.getAllValues());
    }

    @Test
    public void whenIReadPageOfSavedContactsWithPersons_thenPageIsReadAndMapped(){
        //given
        PageRequest pageRequest = new PageRequest(2,3, Sort.Direction.ASC,"kind");
        Page<Contact> page = new PageImpl<>(createPageContact(),pageRequest,3);
        when(contactRepository.findPageWithPersons(pageRequest))
                .thenReturn(page);

        //when
        contactService.readPageOfContacts(2,3,"kind","ASC");

        //then
        verify(contactRepository, times(1)).findPageWithPersons(pageRequest);
        verify(pageMapper, times(1)).mapToResDTO(any(),any());
    }

    @Test
    public void whenIUpdateSavedAddress_thenItIsUpdatedAndMapped(){
        //given
        Address addressToUpdate = (Address) createTestAddress();
        Person jarek = createJarek();
        jarek.addContact(addressToUpdate);

        UpdateAddressReqDTO updateReqDTO = UpdateAddressReqDTO.subBuilder()
                .id(1L)
                .town("Zamość")
                .zipCode("22-410")
                .street("ul. 3 Maja")
                .province("lubelskie")
                .flatNumber("3")
                .build();

        when(addressRepository.findAddressByIdWithPerson(1L)).thenReturn(Optional.ofNullable(addressToUpdate));

        //when
        contactService.updateContact(updateReqDTO);

        //then
        verify(addressRepository,times(1)).save(addressArgumentCaptor.capture());

        Address expectedAddress = Address.builder()
                .town("Zamość")
                .zipCode("22-410")
                .street("ul. 3 Maja")
                .province(Province.lubelskie)
                .flatNumber("3")
                .build();
        Address updatedAddress = addressArgumentCaptor.getValue();
        assertEquals(expectedAddress,updatedAddress);

        verify(contactMapper,times(1)).mapToResWithPerson(addressToUpdate);
    }

    @Test
    public void whenIUpdateSavedEmailAddress_thenItIsUpdatedAndMapped(){
        //given
        EmailAddress emailAddress = (EmailAddress) createTestEmailAddress();
        Person jarek = createJarek();
        jarek.addContact(emailAddress);

        UpdateEmailAddressReqDTO updateReqDTO = new UpdateEmailAddressReqDTO("jarek0@o2.pl",1L);

        when(emailAddressRepository.findEmailAddressByIdWithPerson(1L)).thenReturn(Optional.of(emailAddress));

        //when
        contactService.updateContact(updateReqDTO);

        //then
        verify(emailAddressRepository,times(1)).save(emailAddressArgumentCaptor.capture());

        EmailAddress expectedEmailAddress = new EmailAddress("jarek0@o2.pl");
        EmailAddress updatedEmailAddress = emailAddressArgumentCaptor.getValue();
        assertEquals(expectedEmailAddress,updatedEmailAddress);

        verify(contactMapper,times(1)).mapToResWithPerson(emailAddress);
    }

    @Test
    public void whenIUpdateSavedPhoneNumber_thenItIsUpdatedAndMapped(){
        //given
        PhoneNumber phoneNumber = (PhoneNumber) createTestPhoneNumber();
        Person jarek = createJarek();
        jarek.addContact(phoneNumber);

        UpdatePhoneNumberReqDTO updateReqDTO = new UpdatePhoneNumberReqDTO("111111111",1L);

        when(phoneNumberRepository.findPhoneNumberByIdWithPerson(1L)).thenReturn(Optional.of(phoneNumber));

        //when
        contactService.updateContact(updateReqDTO);

        //then
        verify(phoneNumberRepository,times(1)).save(phoneNumberArgumentCaptor.capture());

        PhoneNumber expectedPhoneNumber = new PhoneNumber("111111111");
        PhoneNumber updatedPhoneNumber = phoneNumberArgumentCaptor.getValue();
        assertEquals(expectedPhoneNumber,updatedPhoneNumber);

        verify(contactMapper,times(1)).mapToResWithPerson(phoneNumber);
    }

    @Test
    public void whenIDeleteSavedContact_thenItIsDeleted(){
        //when
        contactService.deleteContact(1L);

        //then
        verify(contactRepository,times(1)).delete(1L);
    }

    @Test
    public void whenIDeleteAllContacts_thenTheyAreDeleted(){
        //when
        contactService.deleteAllContacts();

        //then
        verify(contactRepository,times(1)).deleteAll();
    }
}
