package com.britenet.contacts.task.domain.contact.subClasses;

import com.britenet.contacts.task.domain.contact.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("e-mail address")

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailAddress extends Contact {
    @Column(unique = true, name = "email_address")
    private String value;

    public EmailAddress(){
        this.kind = "e-mail address";
    }

    public EmailAddress(String value){
        this.kind = "e-mail address";
        this.value = value;
    }

    public String toString(){
        return value;
    }

}
