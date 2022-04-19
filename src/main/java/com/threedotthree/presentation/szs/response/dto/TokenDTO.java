package com.threedotthree.presentation.szs.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    @ApiParam(value = "JWT Access Token", required = true)
    private String accessToken;

    @ApiParam(value = "JWT Refresh Token", required = true)
    private String refreshToken;

    @Override
    public String toString() {
        return "{"
            + "\"accessToken\":\"" + accessToken + "\""
            + ", \"refreshToken\":\"" + refreshToken + "\""
            + "}";
    }
}
