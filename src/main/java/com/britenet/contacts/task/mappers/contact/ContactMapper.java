package com.britenet.contacts.task.mappers.contact;


import com.britenet.contacts.task.DTO.contact.request.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;

public interface ContactMapper {

    ContactResDTO mapToResDTO(Contact contact);

    ContactWithPersonResDTO mapToResWithPerson(Contact contact);

    Contact fromReqDTO(AddressReqDTO contactReqDTO);

    Contact fromReqDTO(PhoneNumberReqDTO contactReqDTO);

    Contact fromReqDTO(EmailAddressReqDTO contactReqDTO);
}
