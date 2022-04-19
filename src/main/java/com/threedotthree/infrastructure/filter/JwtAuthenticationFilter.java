package com.threedotthree.infrastructure.filter;

import com.threedotthree.infrastructure.exception.message.ResponseMessage;
import com.threedotthree.infrastructure.exception.response.ErrorResponse;
import com.threedotthree.infrastructure.exception.response.dto.ErrorDTO;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtTokenUtil.resolveToken(request);
        String refreshToken = jwtTokenUtil.resolveRefreshToken(request);

        try {
            if ((accessToken != null && !accessToken.isEmpty()) || (refreshToken != null && !refreshToken.isEmpty())) { // access 토큰
                // Filter를 거칠 Token
                String token = accessToken != null && !accessToken.isEmpty() ? accessToken : refreshToken;

                // 유효한 access 토큰인지 확인합니다.
                if (jwtTokenUtil.validateToken(token)) {
                    // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                    Authentication authentication = jwtTokenUtil.getAuthentication(token);
                    // SecurityContext 에 Authentication 객체를 저장합니다.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else { // 토큰이 만료인 경우
                    ErrorResponse.of(response, HttpStatus.FORBIDDEN, ErrorDTO.builder().message(ResponseMessage.FORBIDDEN_MSG).build());
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            ErrorResponse.of(response, HttpStatus.FORBIDDEN, ErrorDTO.builder().field("token").message(ResponseMessage.FORBIDDEN_MSG).build());
        } catch (IllegalArgumentException e) {
            ErrorResponse.of(response, HttpStatus.FORBIDDEN, ErrorDTO.builder().field("roles").message(ResponseMessage.FORBIDDEN_MSG).build());
        } catch (Exception e) {
            log.error("================================================");
            log.error("JwtAuthenticationFilter - doFilterInternal() 오류발생");
            log.error("accessToken : {}", accessToken);
            log.error("refreshToken : {}", refreshToken);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("================================================");
            ErrorResponse.of(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.builder().message(ResponseMessage.INTERNAL_SERVER_ERROR_MSG).build());
        }

    }

}