package com.threedotthree.presentation.szs;

import com.threedotthree.application.response.dto.*;
import com.threedotthree.application.user.UserApplication;
import com.threedotthree.domain.model.scrapCalc.ScrapCalcJpaRepository;
import com.threedotthree.domain.model.user.User;
import com.threedotthree.domain.model.user.UserJpaRepository;
import com.threedotthree.infrastructure.exception.*;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import com.threedotthree.infrastructure.utils.SecurityUtil;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SzsServiceTest {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserApplication userApplication;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ScrapCalcJpaRepository scrapCalcJpaRepository;

    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @AfterEach
    void clean() {
        scrapCalcJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("회원가입_200")
    public void signUp_200() throws Exception {

        // given
        SignUpRequest request = SignUpRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .name("홍길동")
            .regNo("860824-1655068")
            .build();

        // when
        SignUpResultDTO signUpResultDTO = userApplication.signUp(request);

        // then
        assertThat(signUpResultDTO.getUserId()).isEqualTo(request.getUserId());
        assertThat(signUpResultDTO.getName()).isEqualTo(request.getName());

    }

    @Test
    @Transactional
    @DisplayName("회원가입_401")
    public void signUp_401() {

        // given
        SignUpRequest request = SignUpRequest.builder()
            .userId("test")
            .password("szs123")
            .name("삼쩜삼")
            .regNo("123456-9876543")
            .build();

        // then
        assertThrows(UnauthorizedException.class, () -> userApplication.signUp(request));

    }

    @Test
    @Transactional
    @DisplayName("회원가입_409")
    public void signUp_409() throws Exception {

        // given
        SignUpRequest request = SignUpRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .name("홍길동")
            .regNo("860824-1655068")
            .build();

        // when
        userApplication.signUp(request);

        // then
        assertThrows(AlreadyDataException.class, () -> userApplication.signUp(request));

    }

    @Test
    @Transactional
    @DisplayName("로그인_200")
    public void login_200() throws Exception {

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        // when
        LoginRequest request = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(request);

        // then
        assertThat(loginResultDTO.getUserId()).isEqualTo(request.getUserId());
        assertThat(user.getPassword()).isEqualTo(SecurityUtil.passwordHash(request.getPassword(), user.getSalt()));

    }

    @Test
    @Transactional
    @DisplayName("로그인_400")
    public void login_400() throws Exception {

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        // when
        LoginRequest request = LoginRequest.builder()
            .userId("test")
            .password("qlqjs1234")
            .build();

        // then
        assertThrows(BadRequestApiException.class, () -> userApplication.login(request));

    }

    @Test
    @Transactional
    @DisplayName("로그인_422")
    public void login_422() throws Exception {

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        // when
        LoginRequest request = LoginRequest.builder()
            .userId("삼쩜삼")
            .password("qlqjs123")
            .build();

        // then
        assertThrows(NotFoundDataException.class, () -> userApplication.login(request));

    }

    @Test
    @Transactional
    @DisplayName("회원정보_200")
    public void me_200() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        // when
        LoginRequest loginRequest = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(loginRequest);
        request.addHeader(HttpHeaders.AUTHORIZATION, loginResultDTO.getAccessToken());

        UserViewDTO userViewDTO = userApplication.me(request);

        // then
        assertThat(userViewDTO.getUserSeqId()).isEqualTo(user.getUserSeqId());
        assertThat(userViewDTO.getUserId()).isEqualTo(loginRequest.getUserId());
        assertThat(userViewDTO.getName()).isEqualTo(user.getName());
        assertThat(userViewDTO.getRegNo()).isEqualTo(SecurityUtil.strToDecrypt(user.getRegNo()));

    }

    @Test
    @Transactional
    @DisplayName("회원정보_조회_토큰만료_403")
    public void meTokenExpired_403() {

        // given
        String invalidToken = "";

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, invalidToken);

        // then
        assertThrows(TokenExpiredException.class, () -> userApplication.me(request));

    }

    @Test
    @Transactional
    @DisplayName("회원정보_조회_고유번호_불일치_403")
    public void meTokenIsNotEqual_403() throws Exception {

        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));

        // given
        User user1 = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());
        user1.passwordAddSalt("qlqjs123");

        User user2 = userJpaRepository.save(User.builder()
            .userId("test2")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("김둘리")
            .regNo(SecurityUtil.strToEncrypt("921108-1582816"))
            .build());
        user2.passwordAddSalt("qlqjs123");

        // when
        LoginRequest loginRequest = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(loginRequest);

        int userSeqId = jwtTokenUtil.getUserSeqId(loginResultDTO.getAccessToken());

        // then
        assertThat(userSeqId).isNotEqualTo(user2.getUserSeqId());

    }

    @Test
    @Transactional
    @DisplayName("유저_스크랩_200")
    public void scrap_200() throws Exception {

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        LoginRequest loginRequest = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(loginRequest);

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, loginResultDTO.getAccessToken());
        ScrapRestAPIInfoDTO scrapRestAPIInfoDTO = userApplication.scrap(request);

        // then
        assertThat(scrapRestAPIInfoDTO.getStatus()).isEqualTo("success");

    }

    @Test
    @Transactional
    @DisplayName("유저_스크랩_403")
    public void scrapTokenExpiredException_403() {

        // given
        String invalidToken = "";

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, invalidToken);

        // then
        assertThrows(TokenExpiredException.class, () -> userApplication.scrap(request));

    }

    @Test
    @DisplayName("환급액_200")
    public void refund_200() throws Exception {

        // given
        String salt = SecurityUtil.generateSalt();
        String hashPassword = SecurityUtil.passwordHash("qlqjs123", salt);

        userJpaRepository.save(User.builder()
            .userId("test")
            .password(hashPassword)
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .salt(salt)
            .build());

        LoginRequest loginRequest = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(loginRequest);

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, loginResultDTO.getAccessToken());
        ScrapRestAPIInfoDTO scrapRestAPIInfoDTO = userApplication.scrap(request);

        // then
        assertThat(scrapRestAPIInfoDTO.getStatus()).isEqualTo("success");

        // when
        RefundDTO refundDTO = userApplication.refund(request);

        // then
        assertThat(refundDTO.get이름()).isEqualTo(loginResultDTO.getName());

    }

    @Test
    @Transactional
    @DisplayName("환급액_400")
    public void refundBadRequestApiException_400() throws Exception {

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        LoginRequest loginRequest = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(loginRequest);

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, loginResultDTO.getAccessToken());

        // then
        assertThrows(BadRequestApiException.class, () -> userApplication.refund(request));

    }

    @Test
    @Transactional
    @DisplayName("환급액_403")
    public void refundTokenExpiredException_403() {

        // given
        String invalidToken = "";

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, invalidToken);

        // then
        assertThrows(TokenExpiredException.class, () -> userApplication.refund(request));

    }

    @Test
    @Transactional
    @DisplayName("회원정보_조회_유저_스크랩_환급액_422")
    public void meAndScrapAndRefundNotFoundDataException_422() throws Exception {

        // given
        User user = userJpaRepository.save(User.builder()
            .userId("test")
            .password(SecurityUtil.passwordHash("qlqjs123", "SZS"))
            .name("홍길동")
            .regNo(SecurityUtil.strToEncrypt("860824-1655068"))
            .build());

        user.passwordAddSalt("qlqjs123");

        LoginRequest loginRequest = LoginRequest.builder()
            .userId("test")
            .password("qlqjs123")
            .build();

        LoginResultDTO loginResultDTO = userApplication.login(loginRequest);

        // when
        request.addHeader(HttpHeaders.AUTHORIZATION, loginResultDTO.getAccessToken());

        userJpaRepository.delete(user);

        // then
        assertThrows(NotFoundDataException.class, () -> userApplication.me(request));
        assertThrows(NotFoundDataException.class, () -> userApplication.scrap(request));
        assertThrows(NotFoundDataException.class, () -> userApplication.refund(request));

    }

}