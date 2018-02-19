package com.britenet.contacts.task.DTO.contact.request.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberReqDTO {
    @NotBlank
    @Pattern(regexp ="^[0-9]{9}$",message = "Invalid phone number format")
    private String value;
}
