package com.threedotthree.szs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.infrastructure.exception.AlreadyDataException;
import com.threedotthree.infrastructure.exception.UnauthorizedException;
import com.threedotthree.presentation.szs.SzsController;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.threedotthree.infrastructure.inMemory.signup.SignUpDTOFactory.mockSignUpAcceptUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SzsController.class)
public class SzsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
            .andExpect(jsonPath("rt").value(401));

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
            .andExpect(jsonPath("rt").value(409));

        assertThat(acceptUser).isTrue();
        verify(userApplication).signUp(any(SignUpRequest.class));

    }

}