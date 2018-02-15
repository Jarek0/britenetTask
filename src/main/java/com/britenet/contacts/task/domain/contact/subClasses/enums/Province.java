package com.britenet.contacts.task.domain.contact.subClasses.enums;

import lombok.ToString;

@ToString
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

    String name;

    Province(String name) {
        this.name = name;
    }
}
