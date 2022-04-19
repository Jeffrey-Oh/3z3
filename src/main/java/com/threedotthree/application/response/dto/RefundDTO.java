package com.threedotthree.application.response.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundDTO {

    @ApiParam(value = "회원 이름", required = true, example = "홍길동")
    private String 이름;

    @ApiParam(value = "한도", example = "68만 4천원")
    private String 한도;

    @ApiParam(value = "공제액", example = "92만 5천원")
    private String 공제액;

    @ApiParam(value = "환급액", example = "68만 4천원")
    private String 환급액;

}
