package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScrapRestAPIData {

    @ApiParam(value = "jsonList", required = true)
    private ScrapRestAPIDetail jsonList;

    @ApiParam(value = "앱 버전", required = true)
    private String appVer;

    @ApiParam(value = "호스트 이름", required = true)
    private String hostNm;

    @ApiParam(value = "workerResDt", required = true)
    private String workerResDt;

    @ApiParam(value = "workerReqDt", required = true)
    private String workerReqDt;

}
