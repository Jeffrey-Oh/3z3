package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap001 {

    @ApiParam(value = "소득내역")
    private String 소득내역;

    @ApiParam(value = "총지급액")
    private String 총지급액;

    @ApiParam(value = "업무시작일")
    private String 업무시작일;

    @ApiParam(value = "기업명")
    private String 기업명;

    @ApiParam(value = "이름")
    private String 이름;

    @ApiParam(value = "지급일")
    private String 지급일;

    @ApiParam(value = "업무종료일")
    private String 업무종료일;

    @ApiParam(value = "주민등록번호")
    private String 주민등록번호;

    @ApiParam(value = "소득구분")
    private String 소득구분;

    @ApiParam(value = "사업자등록번호")
    private String 사업자등록번호;

}
