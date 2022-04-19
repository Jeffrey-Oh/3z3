package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIError {

    @Schema(name = "code", description = "에러 코드", example = "")
    private String code;

    @Schema(name = "message", description = "에러 메시지", example = "")
    private String message;

}
