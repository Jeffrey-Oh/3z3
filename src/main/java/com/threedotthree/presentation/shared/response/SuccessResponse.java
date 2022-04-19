package com.threedotthree.presentation.shared.response;

import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SuccessResponse는 각 API Response Class에서 상속받아 사용
 */
public class SuccessResponse {

    @Schema(name = "rt", description = "결과 코드", required = true, example = "200")
    public int rt;

    @Schema(name = "rtMsg", description = "결과 메시지", required = true, example = ResponseMessage.SUCCESS_MSG)
    public String rtMsg;

    public SuccessResponse() {
        this.rt = 200;
        this.rtMsg = ResponseMessage.SUCCESS_MSG;
    }
    
    public SuccessResponse(String rtMsg) {
        this.rt = 200;
        this.rtMsg = rtMsg;
    }

    @Override
    public String toString() {
        return "{"
            + "\"rt\":\"" + rt + "\""
            + ", \"rtMsg\":\"" + rtMsg + "\""
            + "}";
    }
}
