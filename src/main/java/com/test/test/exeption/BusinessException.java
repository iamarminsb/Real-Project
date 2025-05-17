package com.test.test.exeption;

import jdk.jshell.SnippetEvent;
import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException{
    private final String errorKey;
    private final HttpStatus status;

    public BusinessException(String errorKey, String message, HttpStatus status) {
        super(message);
        this.errorKey = errorKey;
        this.status = status;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
