package com.threedotthree.infrastructure.exception;

import com.threedotthree.infrastructure.exception.message.ResponseMessage;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super(ResponseMessage.FORBIDDEN_MSG);
    }

}
