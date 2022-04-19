package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIDetail {

    @ApiParam(value = "scrap001")
    private List<Scrap001> scrap001;

    @ApiParam(value = "scrap002")
    private List<Scrap002> scrap002;

    @ApiParam(value = "scrap003")
    private List<Scrap003> scrap003;

    @ApiParam(value = "scrap004")
    private List<Scrap004> scrap004;

    @ApiParam(value = "errMsg")
    private String errMsg;

    @ApiParam(value = "company", example = "삼쩜삼")
    private String company;

    @ApiParam(value = "svcCd", example = "test01")
    private String svcCd;

    @ApiParam(value = "userId", example = "1")
    private String userId;

}
