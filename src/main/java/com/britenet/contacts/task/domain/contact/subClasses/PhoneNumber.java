package com.britenet.contacts.task.domain.contact.subClasses;

import com.britenet.contacts.task.domain.contact.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("phone number")

@Data
@EqualsAndHashCode(callSuper = false)
public class PhoneNumber extends Contact {
    @Column(length = 9, unique = true, name = "phone_number")
    private String value;

    public PhoneNumber(){
        this.kind = "phone number";
    }

    public PhoneNumber(String value){
        this.kind = "phone number";
        this.value = value;
    }

    public String toString(){
        return value;
    }

}
