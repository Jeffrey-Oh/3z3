package com.threedotthree.infrastructure.exception;

import lombok.Getter;

@Getter
public class BadRequestApiException extends RuntimeException {

    private final String message;
    private String field;

    public BadRequestApiException(String message) {
        super(message);
        this.message = message;
    }

    public BadRequestApiException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }

}
