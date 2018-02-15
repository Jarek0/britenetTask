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
    public String value;

    public EmailAddress(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }

}
