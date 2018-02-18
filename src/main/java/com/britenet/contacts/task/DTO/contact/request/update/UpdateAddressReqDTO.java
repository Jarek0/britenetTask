package com.britenet.contacts.task.DTO.contact.request.update;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateAddressReqDTO extends AddressReqDTO{
    long id;

    @Builder(builderMethodName = "subBuilder")
    public UpdateAddressReqDTO(String town,
                               String zipCode,
                               String street,
                               String province,
                               String flatNumber,
                               String blockNumber,
                               long id) {
        super(town, zipCode, street, province, flatNumber, blockNumber);
        this.id = id;
    }
}
