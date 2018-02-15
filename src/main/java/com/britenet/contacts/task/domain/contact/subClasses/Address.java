package com.britenet.contacts.task.domain.contact.subClasses;


import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@DiscriminatorValue("address")

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class Address extends Contact {
    @Column(length = 40)
    private String town;
    @Column(length = 6)
    private String zipCode;
    @Column(length = 80)
    private String street;
    @Enumerated(EnumType.STRING)
    private Province province;
    @Column(length = 6)
    private String flatNumber;
    @Column(length = 6)
    private String blockNumber;

    public String toString(){
        StringBuilder addressStringBuilder =  new StringBuilder(street).append(" ");
        if(blockNumber!=null)
            addressStringBuilder.append(blockNumber).append("/");
        return addressStringBuilder.append(flatNumber)
                .append(" ").append(zipCode)
                .append(" ").append(town)
                .append(" woj. ").append(province).toString();
    }
}
