package com.britenet.contacts.task.page;

public enum PageOrder {
    ASC("ASC"),DESC("DESC");

    private String kind;

    PageOrder(String kind){
        this.kind = kind;
    }
}
