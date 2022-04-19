package com.threedotthree.infrastructure.exception;

import lombok.Getter;

@Getter
public class AlreadyDataException extends RuntimeException {

    private final String message;
    private String field;

    public AlreadyDataException() {
        super("이미 존재하는 데이터입니다.");
        this.message = "이미 존재하는 데이터입니다.";
    }

    public AlreadyDataException(String message) {
        super(message);
        this.message = message;
    }

    public AlreadyDataException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }
}