package com.britenet.contacts.task.DTO.contact.request.update;


import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePhoneNumberReqDTO extends PhoneNumberReqDTO{
    long id;

    public UpdatePhoneNumberReqDTO(String value, long id) {
        super(value);
        this.id = id;
    }
}
