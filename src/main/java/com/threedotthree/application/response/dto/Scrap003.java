package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap003 {

    @Schema(name = "주택소지여부", description = "주택소지여부", example = "false")
    private Boolean 주택소지여부;

    @Schema(name = "주택청약가입여부", description = "주택청약가입여부", example = "true")
    private Boolean 주택청약가입여부;

    @Schema(name = "주택청약납입금", description = "주택청약납입금", example = "240,000")
    private String 주택청약납입금;

}
