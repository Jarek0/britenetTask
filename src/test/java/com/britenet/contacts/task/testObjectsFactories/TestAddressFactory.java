package com.britenet.contacts.task.testObjectsFactories;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;

public class TestAddressFactory {

    public static Contact createTestAddress(){
        return Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();
    }

    public static ContactResDTO createTestAddressResDTO(){
        return ContactResDTO.builder()
                .id(1L)
                .kind("address")
                .value("ul. Tomasza Zana 2a/2 22-420 Lublin woj. małopolskie")
                .build();
    }

    public static AddressReqDTO createTestAddressReqDTO(){
        return AddressReqDTO.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Tomasza Zana")
                .province("małopolskie")
                .flatNumber("2")
                .blockNumber("2a")
                .build();
    }

    public static Contact createTestAddress2(){
        return Address.builder()
                .town("Lublin")
                .zipCode("22-420")
                .street("ul. Nadbystrzycka")
                .province(Province.getByName("małopolskie"))
                .flatNumber("2")
                .blockNumber("2a")
                .build();
    }
}
