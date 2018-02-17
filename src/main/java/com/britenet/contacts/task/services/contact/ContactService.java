package com.britenet.contacts.task.services.contact;

import com.britenet.contacts.task.DTO.contact.request.update.UpdateAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdateEmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdatePhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;

import java.util.List;

public interface ContactService {

    List<ContactWithPersonResDTO> readAllContacts();

    ContactWithPersonResDTO readContact(long contactId);

    PageResDTO<ContactWithPersonResDTO> readPageOfContacts(int number, int size, String sortedBy, String order);

    ContactWithPersonResDTO updateContact(UpdateAddressReqDTO contactReqDTO);

    ContactWithPersonResDTO updateContact(UpdateEmailAddressReqDTO contactReqDTO);

    ContactWithPersonResDTO updateContact(UpdatePhoneNumberReqDTO contactReqDTO);

    void deleteContact(long contactId);

    void deleteAllContacts();
}
