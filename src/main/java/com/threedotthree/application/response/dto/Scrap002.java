package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap002 {

    @ApiParam(value = "총사용금액")
    private String 총사용금액;

    @ApiParam(value = "소득구분")
    private String 소득구분;

}
