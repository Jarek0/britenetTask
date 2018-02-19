package com.britenet.contacts.task.mappers.contact;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.mappers.person.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ContactMapperImpl implements ContactMapper{

    private final PersonMapper personMapper;

    @Autowired
    public ContactMapperImpl(@Lazy PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Override
    public Contact fromReqDTO(AddressReqDTO contactReqDTO) {
        return Address.builder()
                .town(contactReqDTO.getTown())
                .zipCode(contactReqDTO.getZipCode())
                .street(contactReqDTO.getStreet())
                .province(Province.getByName(contactReqDTO.getProvince()))
                .flatNumber(contactReqDTO.getFlatNumber())
                .blockNumber(contactReqDTO.getBlockNumber())
                .build();
    }

    @Override
    public Contact fromReqDTO(PhoneNumberReqDTO contactReqDTO) {
        return new PhoneNumber(contactReqDTO.getValue());
    }

    @Override
    public Contact fromReqDTO(EmailAddressReqDTO contactReqDTO) {
        return new EmailAddress(contactReqDTO.getValue());
    }

    @Override
    public ContactResDTO mapToResDTO(Contact contact){
        return ContactResDTO.builder()
                .id(contact.getId())
                .kind(contact.getKind())
                .value(contact.toString())
                .build();
    }

    @Override
    public ContactWithPersonResDTO mapToResWithPerson(Contact contact){
        return ContactWithPersonResDTO.subBuilder()
                .id(contact.getId())
                .kind(contact.getKind())
                .value(contact.toString())
                .person(personMapper.mapToResDTO(contact.getPerson()))
                .build();
    }
}
