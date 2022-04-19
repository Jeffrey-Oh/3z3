package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserViewDTO {

    @Schema(name = "userSeqId", description = "회원 고유번호", required = true, example = "1")
    private int userSeqId;

    @Schema(name = "userId", description = "회원 아이디", required = true, example = "hong12")
    private String userId;

    @Schema(name = "name", description = "회원 이름", required = true, example = "홍길동")
    private String name;

    @Schema(name = "regNo", description = "회원 주민등록번호", required = true, example = "860824-1655068")
    private String regNo;

}
