package org.example.taskmanager.exceptions;

public class StatusNotFoundException extends RuntimeException{

    public StatusNotFoundException(String message) {
        super(message);
    }
}
