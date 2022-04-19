package com.threedotthree.presentation.szs;

import com.threedotthree.application.response.dto.LoginResultDTO;
import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import com.threedotthree.presentation.szs.response.LoginResponse;
import com.threedotthree.presentation.szs.response.SignUpResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
