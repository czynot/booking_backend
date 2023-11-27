package com.booking.exceptions;

public class InvalidPasswordException extends Throwable{

    public InvalidPasswordException(String message) {
        super(message);
    }
}
