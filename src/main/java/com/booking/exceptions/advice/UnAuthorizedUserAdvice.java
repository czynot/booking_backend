package com.booking.exceptions.advice;

import com.booking.exceptions.InvalidPasswordException;
import com.booking.exceptions.UnAuthorizedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UnAuthorizedUserAdvice {

    @ResponseBody
    @ExceptionHandler(UnAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String unAuthorizedUser(UnAuthorizedUserException exception) {
        return exception.getMessage();
    }
}
