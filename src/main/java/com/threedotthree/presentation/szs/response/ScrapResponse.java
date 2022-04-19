package com.threedotthree.presentation.szs.response;

import com.threedotthree.application.response.dto.ScrapRestAPIInfoDTO;
import com.threedotthree.presentation.shared.response.SuccessResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapResponse extends SuccessResponse {

    @Schema(name = "scrapRestAPIInfo", description = "스크랩 정보")
    private ScrapRestAPIInfoDTO scrapRestAPIInfo;

}
