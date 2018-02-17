package com.britenet.contacts.task.DTO.contact.request.update;

import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateEmailAddressReqDTO extends EmailAddressReqDTO{
    long id;

    public UpdateEmailAddressReqDTO(String value, long id) {
        super(value);
        this.id = id;
    }
}
