package com.threedotthree.presentation.response;

import com.threedotthree.infrastructure.exception.message.ResponseMessage;

/**
 * SuccessResponse는 각 API Response Class에서 상속받아 사용
 */
public class SuccessResponse {

    public int rt;

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
