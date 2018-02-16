package com.britenet.contacts.task.DTO.contact.request;

import com.britenet.contacts.task.validators.contact.FlatNumberValidation;
import org.hibernate.validator.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder

@FlatNumberValidation({"blockNumber","flatNumber"})
public class AddressReqDTO implements ContactReqDTO{

    @NotBlank
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]{1,39}$", message = "validation.address.town.pattern")
    private String town;

    @NotBlank
    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "validation.address.zipCode.pattern")
    private String zipCode;

    @NotBlank
    @Pattern(regexp = "^[0-9A-ZĄĆĘŁŃÓŚŹŻ][\\sA-ZĄĆĘŁŃÓŚŹŻ0-9a-ząćęłńóśźż. ]{1,79}$", message = "validation.address.street.pattern")
    private String street;

    @NotBlank
    @Pattern(
            regexp = "^(lubelskie|dolnośląskie|małopolskie|śląskie|zachodiopomorskie|wielkopolskie|opolskie|łódzkie|podlaskie)",
            message = "validation.address.province.pattern"
    )
    private String province;

    private String flatNumber;

    @Pattern(regexp = "^\\d{5}[a-z]{1}$",message = "validation.address.blockNumber.pattern")
    private String blockNumber;
}
