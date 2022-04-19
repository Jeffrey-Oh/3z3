package com.threedotthree.infrastructure.exception;

import lombok.Getter;

@Getter
public class NotFoundDataException extends RuntimeException {
    
    private final String message;
    private String field;

    public NotFoundDataException(String message) {
        super(message);
        this.message = message;
    }

    public NotFoundDataException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }

}