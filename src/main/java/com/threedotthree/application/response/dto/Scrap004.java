package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scrap004 {

    @Schema(name = "수임된세무사", description = "수임된세무사", example = "")
    private String 수임된세무사;

    @Schema(name = "수임동의여부", description = "수임동의여부", example = "false")
    private Boolean 수임동의여부;

    @Schema(name = "수임된세무사연락처", description = "수임된세무사연락처", example = "")
    private String 수임된세무사연락처;

}
