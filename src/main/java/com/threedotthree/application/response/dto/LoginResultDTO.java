package com.threedotthree.application.response.dto;

import com.threedotthree.presentation.szs.response.dto.TokenDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "userSeqId", description = "회원 고유번호", required = true, example = "1")
    private int userSeqId;

    @Schema(name = "userId", description = "회원 아이디", required = true, example = "hong12")
    private String userId;

    @Schema(name = "name", description = "회원 이름", required = true, example = "홍길동")
    private String name;

}
