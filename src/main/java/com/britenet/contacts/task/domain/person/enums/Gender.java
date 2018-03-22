package com.britenet.contacts.task.domain.person.enums;

import com.britenet.contacts.task.exceptions.invalidInput.enumType.InvalidGenderKindException;

public enum Gender {
    MALE("male"), FAMALE("famale"), OTHER("other");

    String kind;

    public static Gender getByKind(String kind){
        for(Gender g : values()){
            if(g.kind.equals(kind))
                return g;
        }
        throw new InvalidGenderKindException();
    }

    Gender(String kind) {
        this.kind = kind;
    }

    public String toString(){
        return kind;
    }
}
