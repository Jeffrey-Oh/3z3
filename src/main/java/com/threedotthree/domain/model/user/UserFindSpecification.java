package com.threedotthree.domain.model.user;


import com.threedotthree.infrastructure.exception.NotFoundDataException;
import com.threedotthree.infrastructure.exception.TokenExpiredException;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class UserFindSpecification {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserJpaRepository userJpaRepository;

    /**
     * 유저 정보 검증
     * @param userId : 아이디
     * @return User
     */
    public User findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId).orElseThrow(() -> new NotFoundDataException("조회된 유저정보가 없습니다.", "User"));
    }

    /**
     * 유저 정보 검증
     * @param request : HttpServletRequest
     * @return User
     */
    public User findByToken(HttpServletRequest request) {
        // 액세스 토큰
        String token = jwtTokenUtil.getAccessToken(request);

        // 토큰 없을 시 인증 실패
        if (token == null || token.isEmpty()) throw new TokenExpiredException();

        int userSeqId = jwtTokenUtil.getUserSeqId(token);

        return userJpaRepository.findByUserSeqId(userSeqId).orElseThrow(() -> new NotFoundDataException("조회된 유저정보가 없습니다.", "User"));
    }
}
