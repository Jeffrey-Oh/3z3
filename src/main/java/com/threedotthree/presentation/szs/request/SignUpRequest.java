package com.threedotthree.presentation.szs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Schema(name = "userId", description = "회원 아이디", required = true, example = "hong12")
    private String userId;

    @Schema(name = "password", description = "회원 패스워드", required = true, example = "123456")
    private String password;

    @Schema(name = "name", description = "회원 이름", required = true, example = "홍길동")
    private String name;

    @Schema(name = "regNo",  description = "주민등록번호", required = true, example = "860824-1655068")
    private String regNo;

}
