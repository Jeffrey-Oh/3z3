package com.threedotthree.presentation.szs.response;

import com.threedotthree.application.response.dto.LoginResultDTO;
import com.threedotthree.presentation.shared.response.SuccessResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends SuccessResponse {

    @Schema(name = "loginResult", description = "로그인 결과 정보")
    private LoginResultDTO loginResult;

}
