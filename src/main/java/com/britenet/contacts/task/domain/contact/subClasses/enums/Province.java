package com.britenet.contacts.task.domain.contact.subClasses.enums;

import com.britenet.contacts.task.exceptions.invalidInput.enumType.InvalidProvinceNameException;

public enum Province {
    lubelskie("lubelskie"),
    dolnoslaskie("dolnośląskie"),
    malopolskie("małopolskie"),
    slaskie("śląskie"),
    zachodiopomorskie("zachodiopomorskie"),
    wielkopolskie("wielkopolskie"),
    opolskie("opolskie"),
    lodzkie("łódzkie"),
    podlaskie("podlaskie");

    public String name;

    Province(String name) {
        this.name = name;
    }

    public static Province getByName(String name){
        for(Province p : values()){
            if(p.name.equals(name))
                return p;
        }
        throw new InvalidProvinceNameException();
    }

    public String toString(){
        return name;
    }
}
