package com.booking.exceptions;

public class UnAuthorizedUserException extends Throwable{

    public UnAuthorizedUserException(){
        super("User not authorized");
    }
}
