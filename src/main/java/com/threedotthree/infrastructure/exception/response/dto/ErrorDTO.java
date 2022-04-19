package com.threedotthree.infrastructure.exception.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDTO {

    @Schema(name = "field", description = "에러 필드")
    private String field;

    @Schema(name = "message", description = "에러 메시지", required = true)
    private String message;

    @Override
    public String toString() {
        return "{"
            + "\"field\":\"" + field + "\""
            + ", \"message\":\"" + message + "\""
            + "}";
    }
}
