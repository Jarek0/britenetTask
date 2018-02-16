package com.britenet.contacts.task.DTO.contact.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class PhoneNumberReqDTO implements ContactReqDTO{
    @NotBlank
    @Pattern(regexp ="^[0-9]{9}$",message = "validation.phoneNumber.value.pattern")
    private String value;
}
