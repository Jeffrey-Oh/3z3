package com.threedotthree.infrastructure.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private String message;
    private String field;

    public UnauthorizedException() {
        super("");
    }

    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }

    public UnauthorizedException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }

}
