package com.britenet.contacts.task.exceptions.invalidInput;


import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.domain.person.Person;

public class DuplicatedContactException extends IllegalArgumentException{
    public DuplicatedContactException(Person person, Contact contact){
        super("Person with pesel: "+person.getPesel()+ " already have "+ contact.getKind()+": "+contact.toString());
    }
}
