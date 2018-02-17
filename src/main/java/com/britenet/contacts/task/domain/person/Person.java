package com.britenet.contacts.task.domain.person;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.exceptions.invalidInput.DuplicatedContactException;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Person implements Serializable{

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

    @Builder
    public Person(String name, String surname, Gender gender, LocalDate birthDate, String pesel) {
        this.contacts = new HashSet<>();
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.pesel = pesel;
    }

    public void addAllContacts(Contact... contacts){
        Arrays.stream(contacts).forEach(this::addContact);
    }

    public void addContact(Contact contact){
        if(contacts.contains(contact))
            throw new DuplicatedContactException(this,contact);

        contact.setPerson(this);
        contacts.add(contact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getPesel(), person.getPesel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPesel());
    }
}
