package com.threedotthree.application.user;


import com.threedotthree.application.response.dto.LoginResultDTO;
import com.threedotthree.application.response.dto.SignUpResultDTO;
import com.threedotthree.application.response.dto.UserViewDTO;
import com.threedotthree.domain.model.user.User;
import com.threedotthree.domain.model.user.UserFindSpecification;
import com.threedotthree.domain.model.user.UserJpaRepository;
import com.threedotthree.infrastructure.exception.AlreadyDataException;
import com.threedotthree.infrastructure.exception.BadRequestApiException;
import com.threedotthree.infrastructure.exception.TokenExpiredException;
import com.threedotthree.infrastructure.exception.UnauthorizedException;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import com.threedotthree.infrastructure.utils.SecurityUtil;
import com.threedotthree.presentation.szs.request.LoginRequest;
import com.threedotthree.presentation.szs.request.SignUpRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserApplication {

    private final ModelMapper modelMapper;
    private final JwtTokenUtil jwtTokenUtil;

    private final UserJpaRepository userJpaRepository;
    private final UserFindSpecification userFindSpecification;

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

        // 액세스 토큰
        String token = jwtTokenUtil.getAccessToken(request);

        // 토큰 없을 시 인증 실패
        if (token == null || token.isEmpty()) throw new TokenExpiredException();

        int userSeqId = jwtTokenUtil.getUserSeqId(token);

        User user = userFindSpecification.findByUserSeqId(userSeqId);

        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        userViewDTO.setRegNo(SecurityUtil.strToDecrypt(user.getRegNo()));

        return userViewDTO;

    }

}
