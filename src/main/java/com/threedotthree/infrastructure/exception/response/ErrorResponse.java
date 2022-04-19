package com.threedotthree.infrastructure.exception.response;

import com.threedotthree.infrastructure.exception.response.dto.ErrorDTO;
import com.threedotthree.infrastructure.utils.JsonUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int rt;

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
