package com.britenet.contacts.task.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiGlobalError {
    private String message;
}
