package com.threedotthree.application.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundDTO {

    @Schema(name = "이름", description = "회원 이름", required = true, example = "홍길동")
    private String 이름;

    @Schema(name = "한도", description = "한도", required = true, example = "68만 4천원")
    private String 한도;

    @Schema(name = "공제액", description = "공제액", required = true, example = "92만 5천원")
    private String 공제액;

    @Schema(name = "환급액", description = "환급액", required = true, example = "68만 4천원")
    private String 환급액;

}
