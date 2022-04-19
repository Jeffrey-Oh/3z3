package com.threedotthree.presentation.szs.response;

import com.threedotthree.application.response.dto.UserViewDTO;
import com.threedotthree.presentation.shared.response.SuccessResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserViewResponse extends SuccessResponse {

    @Schema(name = "userViewDTO", description = "회원 정보")
    private UserViewDTO userViewDTO;

}
