package com.threedotthree.presentation.szs.response;

import com.threedotthree.application.response.dto.UserViewDTO;
import com.threedotthree.presentation.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserViewResponse extends SuccessResponse {

    private UserViewDTO userViewDTO;

}
