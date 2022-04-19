package com.threedotthree.infrastructure.exception.response;

import com.threedotthree.infrastructure.exception.response.dto.ErrorDTO;
import com.threedotthree.infrastructure.utils.JsonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    @Schema(name = "rt", description = "결과 코드", required = true)
    private int rt;

    @Schema(name = "errors", description = "에러 정보", required = true, implementation = ErrorDTO.class)
    private ErrorDTO errors;

    public static ErrorResponse of(HttpStatus httpStatus, ErrorDTO errors) {
        return new ErrorResponse(httpStatus.value(), errors);
    }

    public static void of(HttpServletResponse response, HttpStatus httpStatus, ErrorDTO error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatus.value());
        response.getWriter().write(JsonUtils.convertObjectToJson(ErrorResponse.of(httpStatus, error)));
    }

}
