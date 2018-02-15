package com.britenet.contacts.task.domain.person.enums;

public enum Gender {
    MALE("male"), FAMALE("famale"), OTHER("invalidInput");

    String kind;

    Gender(String kind) {
        this.kind = kind;
    }
}
