package com.threedotthree.infrastructure.exception.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDTO {

    private String field;

    private String message;

    @Override
    public String toString() {
        return "{"
            + "\"field\":\"" + field + "\""
            + ", \"message\":\"" + message + "\""
            + "}";
    }
}
