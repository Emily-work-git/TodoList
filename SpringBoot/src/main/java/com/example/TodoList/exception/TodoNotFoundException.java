package com.example.TodoList.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends IllegalStateException {
    public TodoNotFoundException() {
        super("Todo Not Found");
    }
    public TodoNotFoundException(String message) {
        super(message);
    }
}