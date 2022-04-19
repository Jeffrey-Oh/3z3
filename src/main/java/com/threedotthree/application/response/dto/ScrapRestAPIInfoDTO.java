package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIInfoDTO {

    @Schema(name = "status", description = "통신 결과", required = true, example = "success")
    private String status;

    @Schema(name = "data", description = "데이터", required = true)
    private ScrapRestAPIData data;

    @Schema(name = "errors", description = "에러", example = "{}")
    private ScrapRestAPIError errors;

}
