package com.threedotthree.presentation.szs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedotthree.application.response.dto.*;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.infrastructure.exception.*;
import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import com.threedotthree.infrastructure.exception.response.ErrorResponse;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import com.threedotthree.presentation.szs.response.LoginResponse;
import com.threedotthree.presentation.szs.response.ScrapResponse;
import com.threedotthree.presentation.szs.response.SignUpResponse;
import com.threedotthree.presentation.szs.response.UserViewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.threedotthree.infrastructure.inMemory.signup.SignUpDTOFactory.mockSignUpAcceptUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SzsController.class)
public class SzsControllerTest {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserApplication userApplication;

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NTAyODk5ODEsImV4cCI6MTY1Mjg4MTk4MX0.b638yftGGj3Z8kL4URZenVOgJpjI4wZbGumuu-MLdJk";

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
        String response = result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(HttpStatus.OK.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        SignUpResponse responseDTO = objectMapper.readValue(response, SignUpResponse.class);

        assertThat(acceptUser).isTrue();
        assertThat(responseDTO.rt).isEqualTo(HttpStatus.OK.value());
        assertThat(responseDTO.getSignUpResultDTO().getUserId()).isEqualTo(request.getUserId());
        assertThat(responseDTO.getSignUpResultDTO().getName()).isEqualTo(request.getName());

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
        String response = result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("rt").value(401))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(acceptUser).isFalse();
        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.UNAUTHORIZED_MSG);
        verify(userApplication).signUp(any(SignUpRequest.class));

    }

    @Test
    public void 회원가입409() throws Exception {

        // given
        doThrow(new AlreadyDataException(ResponseMessage.ALREADY_DATA_MSG)).when(userApplication).signUp(any(SignUpRequest.class));

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
        String response = result.andExpect(status().isConflict())
            .andExpect(jsonPath("rt").value(409))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(acceptUser).isTrue();
        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.ALREADY_DATA_MSG);
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
        String response = result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(HttpStatus.OK.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        LoginResponse responseDTO = objectMapper.readValue(response, LoginResponse.class);

        assertThat(responseDTO.getLoginResult().getUserId()).isEqualTo(request.getUserId());
        verify(userApplication).login(any(LoginRequest.class));

    }

    @Test
    public void 로그인_400() throws Exception {

        // given
        doThrow(new BadRequestApiException(ResponseMessage.BAD_REQUEST_MSG)).when(userApplication).login(any(LoginRequest.class));

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
        String response = result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("rt").value(400))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.BAD_REQUEST_MSG);
        verify(userApplication).login(any(LoginRequest.class));

    }

    @Test
    public void 로그인_422() throws Exception {

        // given
        doThrow(new NotFoundDataException(ResponseMessage.NOT_FOUND_DATA_MSG)).when(userApplication).login(any(LoginRequest.class));

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
        String response = result.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("rt").value(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.NOT_FOUND_DATA_MSG);
        verify(userApplication).login(any(LoginRequest.class));

    }

    @Test
    public void 회원정보_조회_200() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(HttpStatus.OK.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        UserViewResponse responseDTO = objectMapper.readValue(response, UserViewResponse.class);

        // 회원 고유번호 추출
        int userSeqId = jwtTokenUtil.getUserSeqId(this.token);

        assertThat(jwtTokenUtil.validateToken(this.token)).isTrue();
        assertThat(responseDTO.getUserViewDTO().getUserSeqId()).isEqualTo(userSeqId);
        verify(userApplication).me(any(HttpServletRequest.class));

    }

    @Test
    public void 회원정보_조회_403() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        String invalidToken = "tokenInValid";

        // given
        doThrow(new TokenExpiredException()).when(userApplication).me(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isForbidden())
            .andExpect(jsonPath("rt").value(403))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.FORBIDDEN_MSG);
        assertThat(jwtTokenUtil.validateToken(invalidToken)).isFalse();
        verify(userApplication).me(any(HttpServletRequest.class));

    }

    @Test
    public void 회원정보_조회_422() throws Exception {

        // given
        doThrow(new NotFoundDataException(ResponseMessage.NOT_FOUND_DATA_MSG)).when(userApplication).me(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("rt").value(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.NOT_FOUND_DATA_MSG);
        verify(userApplication).me(any(HttpServletRequest.class));

    }

    @Test
    public void 유저_스크랩_200() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        // 회원 고유번호
        int userSeqId = 1;

        List<Scrap001> scrap001List = List.of(
            Scrap001.builder()
                .소득내역("급여")
                .총지급액("41,333.333")
                .업무시작일(new Date().toString())
                .기업명("(주)프리저")
                .이름("베지터")
                .지급일(new Date().toString())
                .업무종료일(new Date().toString())
                .주민등록번호("910411-1656116")
                .소득구분("근로소득(연간)")
                .사업자등록번호("012-44-45749")
                .build()
        );

        List<Scrap002> scrap002List = List.of(
            Scrap002.builder()
                .총사용금액("2,000,000")
                .소득구분("산출세액")
                .build()
        );

        List<Scrap003> scrap003List = List.of(
            Scrap003.builder()
                .주택소지여부(false)
                .주택청약가입여부(true)
                .주택청약납입금("240,000")
                .build()
        );

        List<Scrap004> scrap004List = List.of(
            Scrap004.builder()
                .수임된세무사("")
                .수임동의여부(false)
                .수임된세무사연락처("")
                .build()
        );

        ScrapRestAPIDetail jsonList = ScrapRestAPIDetail.builder()
            .scrap001(scrap001List)
            .scrap002(scrap002List)
            .scrap003(scrap003List)
            .scrap004(scrap004List)
            .errMsg("")
            .company("szs")
            .svcCd("test01")
            .userId("1")
            .build();

        ScrapRestAPIData data = ScrapRestAPIData.builder()
            .jsonList(jsonList)
            .appVer("2021112501")
            .hostNm("jobis-codetest")
            .workerResDt(new Date().toString())
            .workerReqDt(new Date().toString())
            .build();

        ScrapRestAPIError errors = ScrapRestAPIError.builder()
            .code(null)
            .message(null)
            .build();

        ScrapRestAPIInfoDTO scrapRestAPIInfoDTO = ScrapRestAPIInfoDTO.builder()
            .status("success")
            .data(data)
            .errors(errors)
            .build();

        // given
        given(userApplication.scrap(any(HttpServletRequest.class))).willReturn(scrapRestAPIInfoDTO);

        // when
        ResultActions result = mockMvc.perform(
            post("/szs/scrap")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(HttpStatus.OK.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ScrapResponse responseDTO = objectMapper.readValue(response, ScrapResponse.class);

        assertThat(jwtTokenUtil.validateToken(this.token)).isTrue();
        assertThat(jwtTokenUtil.getUserSeqId(this.token)).isEqualTo(userSeqId);
        assertThat(responseDTO.getScrapRestAPIInfo().getData().getJsonList().getScrap001().get(0).get이름()).isEqualTo(scrap001List.get(0).get이름());
        verify(userApplication).scrap(any(HttpServletRequest.class));

    }

    @Test
    public void 유저_스크랩_403() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        String invalidToken = "tokenInValid";

        // given
        doThrow(new TokenExpiredException()).when(userApplication).scrap(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            post("/szs/scrap")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isForbidden())
            .andExpect(jsonPath("rt").value(403))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.FORBIDDEN_MSG);
        assertThat(jwtTokenUtil.validateToken(invalidToken)).isFalse();
        verify(userApplication).scrap(any(HttpServletRequest.class));

    }

    @Test
    public void 유저_스크랩_422() throws Exception {

        // given
        doThrow(new NotFoundDataException(ResponseMessage.NOT_FOUND_DATA_MSG)).when(userApplication).scrap(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            post("/szs/scrap")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("rt").value(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.NOT_FOUND_DATA_MSG);
        verify(userApplication).scrap(any(HttpServletRequest.class));

    }

    @Test
    public void 환급액_200() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        RefundDTO refundDTO = RefundDTO.builder()
            .이름("홍길동")
            .한도("68만 4천원")
            .공제액("92만 5천원")
            .환급액("68만 4천원")
            .build();

        // given
        given(userApplication.refund(any(HttpServletRequest.class))).willReturn(refundDTO);

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/refund")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        RefundDTO responseDTO = objectMapper.readValue(response, RefundDTO.class);

        assertThat(jwtTokenUtil.validateToken(this.token)).isTrue();
        assertThat(responseDTO.get이름()).isEqualTo(refundDTO.get이름());
        assertThat(responseDTO.get한도()).isEqualTo(refundDTO.get한도());
        assertThat(responseDTO.get공제액()).isEqualTo(refundDTO.get공제액());
        assertThat(responseDTO.get환급액()).isEqualTo(refundDTO.get환급액());
        verify(userApplication).refund(any(HttpServletRequest.class));

    }

    @Test
    public void 환급액_400() throws Exception {

        // given
        doThrow(new BadRequestApiException(ResponseMessage.BAD_REQUEST_MSG)).when(userApplication).refund(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/refund")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("rt").value(400))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.BAD_REQUEST_MSG);
        verify(userApplication).refund(any(HttpServletRequest.class));

    }

    @Test
    public void 환급액_403() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        String invalidToken = "tokenInValid";

        // given
        doThrow(new TokenExpiredException()).when(userApplication).refund(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/refund")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isForbidden())
            .andExpect(jsonPath("rt").value(403))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.FORBIDDEN_MSG);
        assertThat(jwtTokenUtil.validateToken(invalidToken)).isFalse();
        verify(userApplication).refund(any(HttpServletRequest.class));

    }

    @Test
    public void 환급액_409() throws Exception {

        // given
        doThrow(new AlreadyDataException(ResponseMessage.ALREADY_DATA_MSG)).when(userApplication).refund(any(HttpServletRequest.class));

        // when
        ResultActions result = mockMvc.perform(
            get("/szs/refund")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        String response = result.andExpect(status().isConflict())
            .andExpect(jsonPath("rt").value(409))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse responseDTO = objectMapper.readValue(response, ErrorResponse.class);

        assertThat(responseDTO.getRt()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(responseDTO.getErrors().getMessage()).isEqualTo(ResponseMessage.ALREADY_DATA_MSG);
        verify(userApplication).refund(any(HttpServletRequest.class));

    }

}