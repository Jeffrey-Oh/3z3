package com.threedotthree.infrastructure.annotation;

import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = ResponseMessage.BAD_REQUEST_MSG),
    @ApiResponse(responseCode = "401", description = ResponseMessage.UNAUTHORIZED_MSG),
    @ApiResponse(responseCode = "403", description = ResponseMessage.FORBIDDEN_MSG),
    @ApiResponse(responseCode = "405", description = ResponseMessage.METHOD_NOT_ALLOWED_MSG),
    @ApiResponse(responseCode = "409", description = ResponseMessage.ALREADY_DATA_MSG),
    @ApiResponse(responseCode = "417", description = ResponseMessage.EXPECTATION_FAILED_MSG),
    @ApiResponse(responseCode = "422", description = ResponseMessage.NOT_FOUND_DATA_MSG),
    @ApiResponse(responseCode = "500", description = ResponseMessage.INTERNAL_SERVER_ERROR_MSG)
})
public @interface ApiErrorResponses {
}
