package com.threedotthree.infrastructure.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String message;
    private String field;

    public ValidationException(String message) {
        super(message);
        this.message = message;
    }

    public ValidationException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }

}
