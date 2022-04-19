package com.threedotthree.presentation.szs.request;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @ApiParam(value = "회원 아이디", required = true, example = "hong12")
    private String userId;

    @ApiParam(value = "회원 패스워드", required = true, example = "123456")
    private String password;

}
