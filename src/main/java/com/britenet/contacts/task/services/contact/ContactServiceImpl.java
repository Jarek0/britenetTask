package com.britenet.contacts.task.services.contact;

import com.britenet.contacts.task.DTO.contact.request.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.mappers.contact.ContactMapper;
import com.britenet.contacts.task.mappers.page.PageMapper;
import com.britenet.contacts.task.repository.contact.AddressRepository;
import com.britenet.contacts.task.repository.contact.ContactRepository;
import com.britenet.contacts.task.repository.contact.EmailAddressRepository;
import com.britenet.contacts.task.repository.contact.PhoneNumberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService{

    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final EmailAddressRepository emailAddressRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    private final PageMapper pageMapper;
    private final ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository,
                              AddressRepository addressRepository,
                              EmailAddressRepository emailAddressRepository,
                              PhoneNumberRepository phoneNumberRepository,
                              PageMapper pageMapper,
                              ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.emailAddressRepository = emailAddressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.pageMapper = pageMapper;
        this.contactMapper = contactMapper;
    }

    @Override
    public List<ContactWithPersonResDTO> readAllContacts() {
        List<Contact> contacts = contactRepository.findAllWithPersons();

        return contacts.stream().map(contactMapper::mapToResWithPerson).collect(Collectors.toList());
    }

    @Override
    public ContactWithPersonResDTO readContact(long contactId) {
        Contact foundContact = contactRepository.findByIdWithPerson(contactId)
                .orElseThrow(() -> new ObjectNotFoundException(Contact.class));

        return contactMapper.mapToResWithPerson(foundContact);
    }

    @Override
    public PageResDTO<ContactWithPersonResDTO> readPageOfContacts(int number, int size, String sortedBy, String order) {
        PageRequest pageRequest = new PageRequest(number,size, Sort.Direction.fromString(order),sortedBy);
        Page<Contact> pageOfContacts = contactRepository.findPageWithPersons(pageRequest);

        return pageMapper.mapToResDTO(pageOfContacts,contactMapper::mapToResWithPerson);
    }

    @Override
    public ContactWithPersonResDTO updateContact(long contactId, AddressReqDTO contactReqDTO) {

        Address contactToUpdate = addressRepository.findAddressByIdWithPerson(contactId)
                .orElseThrow(() -> new ObjectNotFoundException(Address.class));

        contactToUpdate.setTown(contactReqDTO.getTown());
        contactToUpdate.setZipCode(contactReqDTO.getZipCode());
        contactToUpdate.setStreet(contactReqDTO.getStreet());
        contactToUpdate.setProvince(Province.getByName(contactReqDTO.getProvince()));
        contactToUpdate.setFlatNumber(contactReqDTO.getFlatNumber());
        contactToUpdate.setBlockNumber(contactReqDTO.getBlockNumber());

        return contactMapper.mapToResWithPerson(contactToUpdate);
    }

    @Override
    public ContactWithPersonResDTO updateContact(long contactId, EmailAddressReqDTO contactReqDTO) {

        EmailAddress contactToUpdate = emailAddressRepository.findEmailAddressByIdWithPerson(contactId)
                .orElseThrow(() -> new ObjectNotFoundException(EmailAddress.class));

        contactToUpdate.setValue(contactReqDTO.getValue());

        return contactMapper.mapToResWithPerson(contactToUpdate);
    }

    @Override
    public ContactWithPersonResDTO updateContact(long contactId, PhoneNumberReqDTO contactReqDTO) {

        PhoneNumber contactToUpdate = phoneNumberRepository.findPhoneNumberByIdWithPerson(contactId)
                .orElseThrow(() -> new ObjectNotFoundException(PhoneNumber.class));

        contactToUpdate.setValue(contactReqDTO.getValue());

        return contactMapper.mapToResWithPerson(contactToUpdate);
    }

    @Override
    public void deleteContact(long contactId) {
        contactRepository.delete(contactId);
    }

    @Override
    public void deleteAllContacts() {
        contactRepository.deleteAll();
    }
}
