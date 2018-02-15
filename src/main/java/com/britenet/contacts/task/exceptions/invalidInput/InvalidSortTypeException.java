package com.britenet.contacts.task.exceptions.invalidInput;

public class InvalidSortTypeException extends RuntimeException{
    public InvalidSortTypeException(){
        super("Invalid sort type");
    }
}
