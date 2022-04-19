package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIData {

    @Schema(name = "jsonList", description = "jsonList", required = true)
    private ScrapRestAPIDetail jsonList;

    @Schema(name = "appVer", description = "앱 버전", required = true, example = "2021112501")
    private String appVer;

    @Schema(name = "hostNm", description = "호스트 이름", required = true, example = "jobis-codetest")
    private String hostNm;

    @Schema(name = "workerResDt", description = "workerResDt", required = true, example = "2022-03-23T02:23:59.689697")
    private String workerResDt;

    @Schema(name = "workerReqDt", description = "workerReqDt", required = true, example = "2022-03-23T02:23:59.690024")
    private String workerReqDt;

}
