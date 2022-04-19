package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResultDTO {

    @ApiModelProperty(notes = "회원 고유번호", required = true, example = "1")
    private int userSeqId;

    @ApiModelProperty(notes = "회원 아이디", required = true, example = "hong12")
    private String userId;

    @ApiModelProperty(notes = "회원 이름", required = true, example = "홍길동")
    private String name;

}
