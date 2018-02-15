package com.britenet.contacts.task.domain.person;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.exceptions.invalidInput.DuplicatedContactException;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity

@Data
@Builder
public class Person{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(nullable = false, length = 30)
    String name;
    @Column(nullable = false, length = 30)
    String surname;
    @Enumerated(EnumType.STRING)
    Gender gender;
    LocalDate birthDate;
    @Column(unique = true, length = 11)
    String pesel;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contact> contacts;

    public void addContact(Contact contact){
        if(contacts.contains(contact))
            throw new DuplicatedContactException(this,contact);

        contact.setPerson(this);
        contacts.add(contact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return Objects.equals(getPesel(), person.getPesel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPesel());
    }
}
