package com.britenet.contacts.task.DTO.person.request;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class UpdatePersonReqDTO {

    long id;

    @NotBlank
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zzżźćńółęąś]{2,29}$",message = "validation.person.name.pattern")
    String name;

    @NotBlank
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zzżźćńółęąś]{2,29}$",message = "validation.person.surname.pattern")
    String surname;

    @NotBlank
    @Pattern(regexp = "^(male|female|other)",message = "validation.person.gender.pattern")
    String gender;

    @NotBlank
    @Past
    LocalDate birthDate;

    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$",message = "validation.person.pesel.pattern")
    String pesel;

}
