package com.britenet.contacts.task.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiFieldError {
    private String field;
    private String message;
}
