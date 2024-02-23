package com.jazy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CustomerDuplicateException extends RuntimeException {
    public CustomerDuplicateException(String message) {
        super(message);
    }
}
