package com.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "Quiz not found.";

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = NOT_FOUND_MESSAGE)
    public HashMap<String, String> handleNoSuchElementException(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", NOT_FOUND_MESSAGE);
        response.put("error", e.getClass().getSimpleName());
        return response;
    }
}

