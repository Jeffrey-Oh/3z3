package com.threedotthree.presentation.szs.response;

import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.presentation.shared.response.SuccessResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse extends SuccessResponse {

    @Schema(name = "signUpResultDTO", description = "회원 가입정보")
    private SignUpResultDTO signUpResultDTO;

}
