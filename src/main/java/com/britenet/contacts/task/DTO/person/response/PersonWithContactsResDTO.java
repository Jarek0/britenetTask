package com.britenet.contacts.task.DTO.person.response;

import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PersonWithContactsResDTO extends PersonResDTO{

    private List<ContactResDTO> contacts;

    @Builder(builderMethodName = "subBuilder")
    PersonWithContactsResDTO(long id,
                             String name,
                             String surname,
                             String gender,
                             String birthDate,
                             String pesel,
                             List<ContactResDTO> contacts) {
        super(id, name, surname, gender, birthDate, pesel);
        this.contacts = contacts;
    }
}
