package com.britenet.contacts.task.domain.contact;

import com.britenet.contacts.task.domain.person.Person;
import lombok.Data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "kind", discriminatorType = DiscriminatorType.STRING)

@Data
public abstract class Contact{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(insertable = false, updatable = false)
    protected String kind;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Person person;
}
