package com.example.czn.exception;

public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(Exception e) {
        super(e);
    }

    public CommandNotFoundException() {

    }
}
