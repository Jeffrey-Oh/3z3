package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap002 {

    @Schema(name = "총사용금액", description = "총사용금액", example = "1,000,000")
    private String 총사용금액;

    @Schema(name = "소득구분", description = "소득구분", example = "산출세액")
    private String 소득구분;

}
