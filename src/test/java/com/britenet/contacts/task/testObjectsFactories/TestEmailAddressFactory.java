package com.britenet.contacts.task.testObjectsFactories;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;

public class TestEmailAddressFactory {

    public static Contact createTestEmailAddress(){
        return new EmailAddress("jery0@o2.pl");
    }

    public static ContactResDTO createTestEmailAddressResDTO(){
        return ContactResDTO.builder()
                .id(3L)
                .kind("e-mail address")
                .value("jery0@o2.pl")
                .build();
    }

    public static Contact createTestEmailAddress2(){
        return new EmailAddress("jery2@o2.pl");
    }
}
