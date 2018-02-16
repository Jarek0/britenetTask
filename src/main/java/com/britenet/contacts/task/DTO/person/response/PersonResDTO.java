package com.britenet.contacts.task.DTO.person.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class PersonResDTO {
    protected long id;
    protected String name;
    protected String surname;
    protected String gender;
    protected String birthDate;
    protected String pesel;
}
