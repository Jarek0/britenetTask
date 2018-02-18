package com.britenet.contacts.task.DTO.contact.response;

import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactWithPersonResDTO extends ContactResDTO{

    private PersonResDTO person;

    @Builder(builderMethodName = "subBuilder")
    public ContactWithPersonResDTO(long id, String kind, String value, PersonResDTO person) {
        super(id, kind, value);
        this.person = person;
    }
}
