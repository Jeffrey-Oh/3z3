package com.threedotthree.szs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedotthree.application.response.dto.LoginResultDTO;
import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.application.response.dto.UserViewDTO;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.infrastructure.exception.*;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import com.threedotthree.presentation.szs.SzsController;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;

import static com.threedotthree.infrastructure.inMemory.signup.SignUpDTOFactory.mockSignUpAcceptUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SzsController.class)
public class SzsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserApplication userApplication;

    @Test
    public void 회원가입_200() throws Exception {

        SignUpResultDTO signUpResultDTO = SignUpResultDTO.builder()
            .userSeqId(1)
            .userId("test")
            .name("홍길동")
            .build();

        // given
        given(userApplication.signUp(any(SignUpRequest.class))).willReturn(signUpResultDTO);

        // when
        SignUpRequest request = SignUpRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .name("홍길동")
            .regNo("860824-1655068")
            .build();

        boolean acceptUser = mockSignUpAcceptUser().containsKey(request.getName()) && mockSignUpAcceptUser().get(request.getName()).equals(request.getRegNo());

        ResultActions result = mockMvc.perform(
            post("/szs/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertThat(acceptUser).isTrue();
        verify(userApplication).signUp(any(SignUpRequest.class));

    }

    @Test
    public void 회원가입_401() throws Exception {

        // given
        doThrow(new UnauthorizedException()).when(userApplication).signUp(any(SignUpRequest.class));

        // when
        SignUpRequest request = SignUpRequest.builder()
            .userId("test")
            .password("szs123")
            .name("삼쩜삼")
            .regNo("123456-9876543")
            .build();

        boolean acceptUser = mockSignUpAcceptUser().containsKey(request.getName()) && mockSignUpAcceptUser().get(request.getName()).equals(request.getRegNo());

        ResultActions result = mockMvc.perform(
            post("/szs/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("rt").value(401))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertThat(acceptUser).isFalse();
        verify(userApplication).signUp(any(SignUpRequest.class));

    }

    @Test
    public void 회원가입_409() throws Exception {

        // given
        doThrow(new AlreadyDataException()).when(userApplication).signUp(any(SignUpRequest.class));

        // when
        SignUpRequest request = SignUpRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .name("홍길동")
            .regNo("860824-1655068")
            .build();

        boolean acceptUser = mockSignUpAcceptUser().containsKey(request.getName()) && mockSignUpAcceptUser().get(request.getName()).equals(request.getRegNo());

        ResultActions result = mockMvc.perform(
            post("/szs/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isConflict())
            .andExpect(jsonPath("rt").value(409))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertThat(acceptUser).isTrue();
        verify(userApplication).signUp(any(SignUpRequest.class));

    }

    @Test
    public void 로그인_200() throws Exception {

        LoginResultDTO loginResultDTO = LoginResultDTO.builder()
            .userSeqId(1)
            .userId("test")
            .name("홍길동")
            .accessToken("accessToken")
            .refreshToken("refreshToken")
            .build();

        // given
        given(userApplication.login(any(LoginRequest.class))).willReturn(loginResultDTO);

        // when
        LoginRequest request = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        ResultActions result = mockMvc.perform(
            post("/szs/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userApplication).login(any(LoginRequest.class));

    }

    @Test
    public void 로그인_400() throws Exception {

        // given
        doThrow(new BadRequestApiException("")).when(userApplication).login(any(LoginRequest.class));

        // when
        LoginRequest request = LoginRequest.builder()
            .userId("test")
            .password("qlqjs1234")
            .build();

        ResultActions result = mockMvc.perform(
            post("/szs/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("rt").value(400))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userApplication).login(any(LoginRequest.class));

    }

    @Test
    public void 로그인_422() throws Exception {

        // given
        doThrow(new NotFoundDataException("")).when(userApplication).login(any(LoginRequest.class));

        // when
        LoginRequest request = LoginRequest.builder()
            .userId("삼쩜삼")
            .password("qlqjs123")
            .build();

        ResultActions result = mockMvc.perform(
            post("/szs/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("rt").value(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userApplication).login(any(LoginRequest.class));

    }

    @Test
    public void 회원정보_조회_200() throws Exception {

        UserViewDTO userViewDTO = UserViewDTO.builder()
            .userSeqId(1)
            .userId("test")
            .name("홍길동")
            .regNo("860824-1655068")
            .build();

        // given
        given(userApplication.me(any(HttpServletRequest.class))).willReturn(userViewDTO);

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userApplication).me(any(HttpServletRequest.class));

    }

    @Test
    public void 회원정보_조회_403() throws Exception {

        // given
        doThrow(new TokenExpiredException()).when(userApplication).me(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/me")
                .header(HttpHeaders.AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden())
            .andExpect(jsonPath("rt").value(403))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userApplication).me(any(HttpServletRequest.class));

    }

    @Test
    public void 회원정보_조회_422() throws Exception {

        // given
        doThrow(new NotFoundDataException("")).when(userApplication).me(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("rt").value(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userApplication).me(any(HttpServletRequest.class));

    }

}