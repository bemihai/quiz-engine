package com.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 13L;

    public EmailAlreadyExistsException(String email) {
        super(String.format("Email %s already exists", email));
    }
}
