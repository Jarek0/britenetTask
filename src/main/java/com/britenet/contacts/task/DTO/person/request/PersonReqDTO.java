package com.britenet.contacts.task.DTO.person.request;

import com.britenet.contacts.task.deserializer.LocalDateDeserializer;
import com.britenet.contacts.task.deserializer.LocalDateSerializer;
import com.britenet.contacts.task.validators.person.Past;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PersonReqDTO {

    @NotNull
    @Pattern(
            regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zzżźćńółęąś]{2,29}$",
            message = "Name must start with big letter and have between 3 and 30 letters"
    )
    String name;

    @NotNull
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zzżźćńółęąś]{2,29}$",
            message = "Surname must start with big letter and have between 3 and 30 letters"
    )
    String surname;

    @NotNull
    @Pattern(regexp = "^(male|female|other)",message = "You can chose one of three kind of gender: male, female or other")
    String gender;

    @Past(message = "You can not chose future date as birth date of person or have more than 200 years old")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate birthDate;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$",message = "Invalid pesel format")
    String pesel;

    @Builder
    public PersonReqDTO(String name, String surname, String gender, LocalDate birthDate, String pesel) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.pesel = pesel;
    }
}
