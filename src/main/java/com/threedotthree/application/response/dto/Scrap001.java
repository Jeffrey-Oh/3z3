package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap001 {

    @Schema(name = "소득내역", description = "소득내역", example = "급여")
    private String 소득내역;

    @Schema(name = "총지급액", description = "총지급액", example = "24,000,000")
    private String 총지급액;

    @Schema(name = "업무시작일", description = "업무시작일", example = "2018.09.03")
    private String 업무시작일;

    @Schema(name = "기업명", description = "기업명", example = "(주) 삼쩜삼")
    private String 기업명;

    @Schema(name = "이름", description = "이름", example = "홍길동")
    private String 이름;

    @Schema(name = "지급일", description = "지급일", example = "2020.10.02")
    private String 지급일;

    @Schema(name = "업무종료일", description = "업무종료일", example = "2018.10.02")
    private String 업무종료일;

    @Schema(name = "주민등록번호", description = "주민등록번호", example = "921108-1582816")
    private String 주민등록번호;

    @Schema(name = "소득구분", description = "소득구분", example = "근로소득(연간)")
    private String 소득구분;

    @Schema(name = "사업자등록번호", description = "사업자등록번호", example = "010-44-55589")
    private String 사업자등록번호;

}
