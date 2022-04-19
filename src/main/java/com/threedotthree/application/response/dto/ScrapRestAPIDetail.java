package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIDetail {

    @Schema(name = "scrap001", description = "scrap001")
    private List<Scrap001> scrap001;

    @Schema(name = "scrap002", description = "scrap002")
    private List<Scrap002> scrap002;

    @Schema(name = "scrap003", description = "scrap003")
    private List<Scrap003> scrap003;

    @Schema(name = "scrap004", description = "scrap004")
    private List<Scrap004> scrap004;

    @Schema(name = "errMsg", description = "errMsg", example = "에러 메시지")
    private String errMsg;

    @Schema(name = "company", description = "company", example = "삼쩜삼")
    private String company;

    @Schema(name = "svcCd", description = "svcCd", example = "test01")
    private String svcCd;

    @Schema(name = "userId", description = "userId", example = "1")
    private String userId;

}
