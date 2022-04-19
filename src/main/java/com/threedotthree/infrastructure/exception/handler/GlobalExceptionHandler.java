package com.threedotthree.infrastructure.exception.handler;

import com.threedotthree.infrastructure.exception.*;
import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import com.threedotthree.infrastructure.exception.response.ErrorResponse;
import com.threedotthree.infrastructure.exception.response.dto.ErrorDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청
     * HttpStatus 400
     */
    @ExceptionHandler(BadRequestApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse BadRequestApiException(BadRequestApiException e) {
        if (e.getMessage().isEmpty()) {
            return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorDTO.builder().field(e.getField()).message(ResponseMessage.BAD_REQUEST_MSG).build());
        }
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorDTO.builder().field(e.getField()).message(e.getMessage()).build());
    }

    /**
     * 인증 실패
     * HttpStatus 401
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse UnauthorizedException(UnauthorizedException e) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorDTO.builder().field(e.getField()).message((e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : ResponseMessage.UNAUTHORIZED_MSG)).build());
    }

    /**
     * 권한 없음
     * HttpStatus 403
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse TokenExpiredException(TokenExpiredException e) {
        return ErrorResponse.of(HttpStatus.FORBIDDEN, ErrorDTO.builder().message(ResponseMessage.FORBIDDEN_MSG).build());
    }

    /**
     * 허용되지 않는 방법(Request Method - GET, POST, PUT, DELETE)
     * HttpStatus 405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, ErrorDTO.builder().message(ResponseMessage.METHOD_NOT_ALLOWED_MSG).build());
    }

    /**
     * 처리할 수 없는 엔티티 (이미 존재하는 데이터로 인해 처리 불가, 저장/수정/삭제 실패)
     * HttpStatus 409
     */
    @ExceptionHandler(AlreadyDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse AlreadyDataException(AlreadyDataException e) {
        return ErrorResponse.of(HttpStatus.CONFLICT, ErrorDTO.builder().field(e.getField()).message((e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : "")).build());
    }

    /**
     * Validation 실패 (RequestBody)
     * HttpStatus 417
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ErrorDTO.builder().field(e.getBindingResult().getFieldError().getField()).message(e.getBindingResult().getFieldError().getDefaultMessage()).build());
    }

    /**
     * Validation 실패 (RequestBody)
     * HttpStatus 417
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ErrorDTO.builder().field(e.getName()).message(ResponseMessage.METHOD_ARGUMENT_TYPE_MISMATCH).build());
    }

    /**
     * Validation 실패 (RequestParam)
     * HttpStatus 417
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse MissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ErrorDTO.builder().field(e.getParameterName()).message(e.getMessage()).build());
    }

    /**
     * Validation 실패 (formValidation)
     * HttpStatus 417
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse ValidationException(ValidationException e) {
        return ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, ErrorDTO.builder().field(e.getField()).message(e.getMessage()).build());
    }

    /**
     * 데이터 조회 실패 (데이터 조회 실패로 인한 처리 불가, 저장/수정/삭제 실패)
     * HttpStatus 422
     */
    @ExceptionHandler(NotFoundDataException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse NotFoundDataException(NotFoundDataException e) {
        return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, ErrorDTO.builder().field(e.getField()).message(e.getMessage()).build());
    }

    /**
     * 알수 없는 오류(내부 서버 오류)
     * httpStatus 500
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        // JWT catch가 되지 않은 관계로 500 내부에서 처리
        if (StringUtils.isNotBlank(e.getMessage()) && e.getMessage().equals("JWT String argument cannot be null or empty.")) {
            return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorDTO.builder().message(ResponseMessage.UNAUTHORIZED_MSG).build());
        }

        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.builder().message(ResponseMessage.INTERNAL_SERVER_ERROR_MSG).build());
    }

}
