package com.britenet.contacts.task.DTO.contact.request.create;

import com.britenet.contacts.task.validators.contact.FlatNumberValidation;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@FlatNumberValidation.List(
        @FlatNumberValidation(
                field = "flatNumber",
                fieldMatch = "blockNumber",
                message = "Invalid flat number format"
        )
)
public class AddressReqDTO {

    @NotBlank
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]{1,39}$",
            message = "Town name must start with big latter and has from 2 to 40 letters")
    private String town;

    @NotBlank
    @Pattern(regexp = "^\\d{2}-\\d{3}$",
            message = "Invalid zip message format")
    private String zipCode;

    @NotBlank
    @Pattern(regexp = "^(ulica |ul. |Aleja |Al. |[A-ZĄĆĘŁŃÓŚŹŻ])[\\sA-ZĄĆĘŁŃÓŚŹŻ0-9a-ząćęłńóśźż. ]{1,79}$",
            message = "Invalid street name")
    private String street;

    @NotBlank
    @Pattern(
            regexp = "^(lubelskie|dolnośląskie|małopolskie|śląskie|zachodiopomorskie|wielkopolskie|opolskie|łódzkie|podlaskie)",
            message = "Invalid province name"
    )
    private String province;

    private String flatNumber;

    @Pattern(regexp = "^\\d{1,5}[a-z]?$",
            message = "Invalid block number format")
    private String blockNumber;

    @Builder
    public AddressReqDTO(String town, String zipCode, String street, String province, String flatNumber, String blockNumber) {
        this.town = town;
        this.zipCode = zipCode;
        this.street = street;
        this.province = province;
        this.flatNumber = flatNumber;
        this.blockNumber = blockNumber;
    }
}
