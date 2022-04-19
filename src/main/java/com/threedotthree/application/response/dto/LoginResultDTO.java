package com.threedotthree.application.response.dto;

import com.threedotthree.presentation.szs.response.dto.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResultDTO extends TokenDTO {

    private int userSeqId;

    private String userId;

    private String name;

}
