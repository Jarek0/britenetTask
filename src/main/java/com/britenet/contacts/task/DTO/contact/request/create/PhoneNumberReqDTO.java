package com.britenet.contacts.task.DTO.contact.request.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberReqDTO {
    @NotNull
    @Pattern(regexp ="^[0-9]{9}$",message = "Invalid phone number format")
    private String value;
}
