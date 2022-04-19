package com.threedotthree.infrastructure.filter;

import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtTokenUtil.resolveToken(request);
        String refreshToken = jwtTokenUtil.resolveRefreshToken(request);

        if ((accessToken != null && !accessToken.isEmpty()) || (refreshToken != null && !refreshToken.isEmpty())) { // access 토큰
            // Filter를 거칠 Token
            String token = accessToken != null && !accessToken.isEmpty() ? accessToken : refreshToken;

            // 유효한 access 토큰인지 확인합니다.
            if (jwtTokenUtil.validateToken(token)) {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                Authentication authentication = jwtTokenUtil.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);

    }

}
