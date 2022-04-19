package com.threedotthree.application.user;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threedotthree.application.response.dto.*;
import com.threedotthree.domain.model.scrapCalc.ScrapCalc;
import com.threedotthree.domain.model.scrapCalc.ScrapCalcJpaRepository;
import com.threedotthree.domain.model.user.User;
import com.threedotthree.domain.model.user.UserFindSpecification;
import com.threedotthree.domain.model.user.UserJpaRepository;
import com.threedotthree.infrastructure.exception.AlreadyDataException;
import com.threedotthree.infrastructure.exception.BadRequestApiException;
import com.threedotthree.infrastructure.exception.UnauthorizedException;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import com.threedotthree.infrastructure.utils.SecurityUtil;
import com.threedotthree.infrastructure.utils.Utils;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserApplication {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final RestTemplate restTemplate;

    private final UserJpaRepository userJpaRepository;
    private final UserFindSpecification userFindSpecification;
    private final ScrapCalcJpaRepository scrapCalcJpaRepository;

    /**
     * UserRole 확인
     */
    public Boolean userRolesCheck(String token) {
        int seqId = jwtTokenUtil.getUserSeqId(token);
        Claims claims = jwtTokenUtil.getClaims(token);
        String roles = String.valueOf(claims.get("roles"));

        if ("ROLE_USER".equals(roles)) {
            User user = userJpaRepository.findByUserSeqId(seqId).orElse(null);
            return user != null;
        } else {
            return false;
        }
    }

    /**
     * 회원가입
     * @param request : 회원가입 정보
     * @return SignUpResponse
     */
    @Transactional(rollbackFor = Exception.class)
    public SignUpResultDTO signUp(SignUpRequest request) throws Exception {

        Map<String, String> acceptUser = new HashMap<>();
        acceptUser.put("홍길동", "860824-1655068");
        acceptUser.put("김둘리", "921108-1582816");
        acceptUser.put("마징가", "880601-2455116");
        acceptUser.put("베지터", "910411-1656116");
        acceptUser.put("손오공", "820326-2715702");

        // 회원가입 가능한 유저인지 체크
        if (acceptUser.containsKey(request.getName()) && acceptUser.get(request.getName()).equals(request.getRegNo())) {
            // 회원정보 조회
            User user = userJpaRepository.findByNameAndRegNo(request.getName(), SecurityUtil.strToEncrypt(request.getRegNo())).orElse(null);

            // 회원가입 한 적이 없다면 ?
            if (user == null) {
                // 아이디 중복검사
                User duplicateUser = userJpaRepository.findByUserId(request.getUserId()).orElse(null);
                if (duplicateUser != null) throw new AlreadyDataException("해당 아이디는 사용할 수 없습니다.", "userId");

                user = userJpaRepository.save(User.builder()
                    .userId(request.getUserId())
                    .password(SecurityUtil.passwordHash(request.getPassword(), "SZS"))
                    .name(request.getName())
                    .regNo(SecurityUtil.strToEncrypt(request.getRegNo()))
                    .build());

                // 비밀번호 보안처리
                user.passwordAddSalt(request.getPassword());

                return SignUpResultDTO.builder()
                    .userSeqId(user.getUserSeqId())
                    .userId(user.getUserId())
                    .name(user.getName())
                    .build();
            } else
                throw new AlreadyDataException("이미 회원가입된 정보가 존재합니다.", "user");
        } else throw new UnauthorizedException("회원가입 권한이 없습니다.");

    }

    /**
     * 로그인
     * @param request : 로그인 정보
     * @return LoginResultDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResultDTO login(LoginRequest request) {

        // 회원가입 여부
        User user = userFindSpecification.findByUserId(request.getUserId());

        // 비밀번호 확인
        if (!user.passwordVerify(request.getPassword()))
            throw new BadRequestApiException("비밀번호가 일치하지 않습니다.", "password");

        String accessToken = jwtTokenUtil.createAccessToken(user.getUserSeqId(), "ROLE_USER");
        String refreshToken = jwtTokenUtil.createRefreshToken(user.getUserSeqId(), "ROLE_USER");

        // refreshToken 업데이트
        user.refreshTokenUpdate(refreshToken);

        return LoginResultDTO.builder()
            .userSeqId(user.getUserSeqId())
            .userId(user.getUserId())
            .name(user.getName())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    /**
     * 회원정보 조회
     * @param request : HttpServletRequest
     * @return UserViewDTO
     */
    public UserViewDTO me(HttpServletRequest request) throws Exception {

        User user = userFindSpecification.findByToken(request);

        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        userViewDTO.setRegNo(SecurityUtil.strToDecrypt(user.getRegNo()));

        return userViewDTO;

    }

    /**
     * 유저 스크랩
     * @param request : HttpServletRequest
     * @return ScrapInfoDTO
     */
    public ScrapRestAPIInfoDTO scrap(HttpServletRequest request) throws Exception {

        User user = userFindSpecification.findByToken(request);

        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        userViewDTO.setRegNo(SecurityUtil.strToDecrypt(user.getRegNo()));

        // 스크랩 API 호출
        String apiUrl = "https://codetest.3o3.co.kr/v1/scrap";

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", userViewDTO.getName());
        jsonObject.put("regNo", userViewDTO.getRegNo());

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), header);

        String result = restTemplate.postForObject(
            apiUrl,
            entity,
            String.class
        );

        JsonNode root = objectMapper.readTree(result);
        ScrapRestAPIInfoDTO scrapRestAPIInfoDTO = objectMapper.treeToValue(root, ScrapRestAPIInfoDTO.class);

        // 환급액 산출에 필요한 데이터 저장
        if (scrapRestAPIInfoDTO.getData() != null) {
            ScrapRestAPIData scrapRestAPIData = scrapRestAPIInfoDTO.getData();
            if (scrapRestAPIData.getJsonList() != null) {
                ScrapRestAPIDetail scrapRestAPIDetail = scrapRestAPIData.getJsonList();

                long income = 0;
                long tax = 0;

                // 총급여액 추출
                if (scrapRestAPIDetail.getScrap001().size() > 0) {
                    List<Scrap001> scrap001List = scrapRestAPIDetail.getScrap001();
                    for (Scrap001 scrap001 : scrap001List) {
                        if (scrap001.get소득내역().equals("급여")) {
                            income = Utils.getNumberToLongValue(scrap001.get총지급액());
                            break;
                        }
                    }
                }

                // 세액 추출
                if (scrapRestAPIDetail.getScrap002().size() > 0) {
                    List<Scrap002> scrap002List = scrapRestAPIDetail.getScrap002();
                    for (Scrap002 scrap002 : scrap002List) {
                        if (scrap002.get소득구분().equals("산출세액")) {
                            tax = Utils.getNumberToLongValue(scrap002.get총사용금액());
                            break;
                        }
                    }
                }

                // 데이터 저장
                if (income > 0 && tax > 0) {
                    scrapCalcJpaRepository.save(ScrapCalc.builder()
                        .user(user)
                        .income(income)
                        .tax(tax)
                        .build());
                }
            }
        }

        return scrapRestAPIInfoDTO;

    }

}
