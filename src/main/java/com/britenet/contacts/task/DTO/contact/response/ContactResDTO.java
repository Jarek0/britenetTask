package com.britenet.contacts.task.DTO.contact.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContactResDTO {
    long id;
    String kind;
    String value;
}
