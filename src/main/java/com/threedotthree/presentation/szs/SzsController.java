package com.threedotthree.presentation.szs;

import com.threedotthree.application.response.dto.*;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import com.threedotthree.presentation.szs.response.LoginResponse;
import com.threedotthree.presentation.szs.response.ScrapResponse;
import com.threedotthree.presentation.szs.response.SignUpResponse;
import com.threedotthree.presentation.szs.response.UserViewResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/szs")
public class SzsController {

    private final UserApplication userApplication;

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/signup")
    public SignUpResponse signup(@Valid @RequestBody SignUpRequest request) throws Exception {
        log.info("Request : {}", request);

        SignUpResultDTO signUpResultDTO = userApplication.signUp(request);
        return new SignUpResponse(signUpResultDTO);
    }

    @ApiOperation(value = "로그인")
    @PostMapping(value = "/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("Request : {}", request);

        LoginResultDTO loginResultDTO = userApplication.login(request);
        return new LoginResponse(loginResultDTO);
    }

    @ApiOperation(value = "회원정보 조회")
    @GetMapping(value = "/me")
    public UserViewResponse me(HttpServletRequest request) throws Exception {
        log.info("Request : {}", request);

        UserViewDTO userViewDTO = userApplication.me(request);
        return new UserViewResponse(userViewDTO);
    }

    @ApiOperation(value = "유저 스크랩")
    @PostMapping(value = "/scrap")
    public ScrapResponse scrap(HttpServletRequest request) throws Exception {
        log.info("Request : {}", request);

        ScrapRestAPIInfoDTO scrapRestAPIInfoDTO = userApplication.scrap(request);
        return new ScrapResponse(scrapRestAPIInfoDTO);
    }

    @ApiOperation(value = "환급액")
    @GetMapping(value = "/refund")
    public RefundDTO refund(HttpServletRequest request) {
        log.info("Request : {}", request);

        return userApplication.refund(request);
    }

}
