package com.britenet.contacts.task.DTO.contact.request.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAddressReqDTO {
    @NotBlank
    @Pattern(
            regexp = "(^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.+[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@+(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$)",
            message = "Invalid email address format"
    )
    private String value;
}
