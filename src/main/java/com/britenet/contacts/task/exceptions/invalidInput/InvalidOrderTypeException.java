package com.britenet.contacts.task.exceptions.invalidInput;

public class InvalidOrderTypeException extends RuntimeException{
    public InvalidOrderTypeException(){
        super("You can chose one of two orderBy types: ASC or DESC");
    }
}
