package com.britenet.contacts.task.services.contact;

import com.britenet.contacts.task.DTO.contact.request.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ContactService {

    List<ContactWithPersonResDTO> readAllContacts();

    ContactWithPersonResDTO readContact(long contactId);

    PageResDTO<ContactWithPersonResDTO> readPageOfContacts(int number, int size, String sortedBy, String order);

    ContactWithPersonResDTO updateContact(long contactId, AddressReqDTO contactReqDTO);

    ContactWithPersonResDTO updateContact(long contactId, EmailAddressReqDTO contactReqDTO);

    ContactWithPersonResDTO updateContact(long contactId, PhoneNumberReqDTO contactReqDTO);

    void deleteContact(long contactId);

    void deleteAllContacts();
}
