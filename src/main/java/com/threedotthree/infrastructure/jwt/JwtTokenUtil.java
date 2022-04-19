package com.threedotthree.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final String TOKEN_PREFIX = "Bearer ";

    // access 토큰 유효시간 2시간
    private long accessTokenValidTime = 2 * 60 * 60 * 1000L;

    // refresh 토큰 유효시간 2주
    private long refreshTokenValidTime = 2 * 7 * 24 * 60 * 60 * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // access 토큰 생성 요청
    public String createAccessToken(int userId, String role) {
        return createToken(accessTokenValidTime, String.valueOf(userId), role);
    }

    // refresh 토큰 생성 요청
    public String createRefreshToken(int userId, String role) {
        return createToken(refreshTokenValidTime, String.valueOf(userId), role);
    }

    // JWT 토큰 인증정보 생성
    public String createToken(long tokenValidTIme, String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId); // JWT playload에 저장되는 정보 단위
        claims.put("roles", role); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setIssuedAt(now) // 토큰 발행시간 정보
            .setExpiration(new Date(now.getTime() + tokenValidTIme)) // set Expire Time (유효기간)
            .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 값 셋팅
            .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // JWT 토큰 인증정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        List<String> roles = new ArrayList<>();
        roles.add(claims.get("roles", String.class));

        Collection<? extends GrantedAuthority> getAuthorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims, "", getAuthorities);
    }

    public String getAccessToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    // 토큰 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 회원정보 추출
    public int getUserSeqId(String token) {
        token = token.replace(TOKEN_PREFIX, "").trim();
        String result = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return Integer.parseInt(result);
    }

}
