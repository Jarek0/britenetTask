package com.britenet.contacts.task.DTO.person.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePersonReqDTO extends PersonReqDTO{

    long id;

    @Builder(builderMethodName = "subBuilder")
    public UpdatePersonReqDTO(String name, String surname, String gender, LocalDate birthDate, String pesel, long id) {
        super(name, surname, gender, birthDate, pesel);
        this.id = id;
    }
}
