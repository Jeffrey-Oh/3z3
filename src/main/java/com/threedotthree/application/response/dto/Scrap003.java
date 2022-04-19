package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap003 {

    @ApiParam(value = "주택소지여부")
    private Boolean 주택소지여부;

    @ApiParam(value = "주택청약가입여부")
    private Boolean 주택청약가입여부;

    @ApiParam(value = "주택청약납입금")
    private String 주택청약납입금;

}
