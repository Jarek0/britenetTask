package com.britenet.contacts.task.testObjectsFactories;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;

public class TestPhoneNumberFactory {

    public static Contact createTestPhoneNumber(){
        return new PhoneNumber("999999999");
    }

    public static ContactResDTO createTestPhoneNumberResDTO(){
        return ContactResDTO.builder()
                .id(2L)
                .kind("phone number")
                .value("999999999")
                .build();
    }

    public static Contact createTestPhoneNumber2(){
        return new PhoneNumber("999999998");
    }
}
