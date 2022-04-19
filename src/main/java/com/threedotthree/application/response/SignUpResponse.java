package com.threedotthree.application.response;

import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.presentation.response.SuccessResponse;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse extends SuccessResponse {

    private SignUpResultDTO signUpResultDTO;

}
