package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap004 {

    @ApiParam(value = "수임된세무사")
    private String 수임된세무사;

    @ApiParam(value = "수임동의여부")
    private Boolean 수임동의여부;

    @ApiParam(value = "수임된세무사연락처")
    private String 수임된세무사연락처;

}
