package com.britenet.contacts.task.DTO.contact.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Builder
@EqualsAndHashCode(exclude = "id")
public class ContactResDTO {
    long id;
    String kind;
    String value;
}
