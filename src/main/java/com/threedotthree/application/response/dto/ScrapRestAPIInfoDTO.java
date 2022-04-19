package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIInfoDTO {

    @ApiParam(value = "통신 결과", required = true, example = "success")
    private String status;

    @ApiParam(value = "데이터", required = true)
    private ScrapRestAPIData data;

    @ApiParam(value = "에러")
    private ScrapRestAPIError errors;

}
