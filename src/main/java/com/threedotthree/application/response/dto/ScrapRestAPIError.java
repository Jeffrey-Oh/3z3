package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIError {

    @ApiParam(value = "에러 코드")
    private String code;

    @ApiParam(value = "에러 메시지")
    private String message;

}
