package com.threedotthree.presentation.szs;

import com.threedotthree.application.response.dto.*;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.infrastructure.annotation.ApiErrorResponses;
import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import com.threedotthree.presentation.szs.response.LoginResponse;
import com.threedotthree.presentation.szs.response.ScrapResponse;
import com.threedotthree.presentation.szs.response.SignUpResponse;
import com.threedotthree.presentation.szs.response.UserViewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/szs", produces = MediaType.APPLICATION_JSON_VALUE)
public class SzsController {

    private final UserApplication userApplication;

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "200", description = ResponseMessage.SUCCESS_MSG, content = @Content(schema = @Schema(implementation = SignUpResponse.class)))
    @ApiErrorResponses
    @PostMapping(value = "/signup")
    public SignUpResponse signup(@Valid @RequestBody SignUpRequest request) throws Exception {
        log.info("Request : {}", request);

        SignUpResultDTO signUpResultDTO = userApplication.signUp(request);
        return new SignUpResponse(signUpResultDTO);
    }

    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = ResponseMessage.SUCCESS_MSG, content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiErrorResponses
    @PostMapping(value = "/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("Request : {}", request);

        LoginResultDTO loginResultDTO = userApplication.login(request);
        return new LoginResponse(loginResultDTO);
    }

    @Operation(summary = "회원정보 조회")
    @ApiResponse(responseCode = "200", description = ResponseMessage.SUCCESS_MSG, content = @Content(schema = @Schema(implementation = UserViewResponse.class)))
    @ApiErrorResponses
    @GetMapping(value = "/me")
    public UserViewResponse me(HttpServletRequest request) throws Exception {
        log.info("Request : {}", request);

        UserViewDTO userViewDTO = userApplication.me(request);
        return new UserViewResponse(userViewDTO);
    }

    @Operation(summary = "유저 스크랩")
    @ApiResponse(responseCode = "200", description = ResponseMessage.SUCCESS_MSG, content = @Content(schema = @Schema(implementation = ScrapResponse.class)))
    @ApiErrorResponses
    @PostMapping(value = "/scrap")
    public ScrapResponse scrap(HttpServletRequest request) throws Exception {
        log.info("Request : {}", request);

        ScrapRestAPIInfoDTO scrapRestAPIInfoDTO = userApplication.scrap(request);
        return new ScrapResponse(scrapRestAPIInfoDTO);
    }

    @Operation(summary = "환급액")
    @ApiResponse(responseCode = "200", description = ResponseMessage.SUCCESS_MSG, content = @Content(schema = @Schema(implementation = RefundDTO.class)))
    @ApiErrorResponses
    @GetMapping(value = "/refund")
    public RefundDTO refund(HttpServletRequest request) {
        log.info("Request : {}", request);
        return userApplication.refund(request);
    }

}
