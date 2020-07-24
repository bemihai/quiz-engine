package com.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotEligibleException extends Exception {

    private static final long serialVersionUID = 11L;

    public UserNotEligibleException(String message) {
        super(message);
    }

    public UserNotEligibleException(String message, Throwable t) {
        super(message, t);
    }
}
