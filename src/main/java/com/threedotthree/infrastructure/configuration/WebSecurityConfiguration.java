package com.threedotthree.infrastructure.configuration;

import com.threedotthree.application.user.UserApplication;
import com.threedotthree.infrastructure.filter.JwtAuthenticationFilter;
import com.threedotthree.infrastructure.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserApplication userApplication;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**")
            .antMatchers("/swagger-ui/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제
            .csrf().disable() // csrf 보안 토큰 disable
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 인증으로 세션 사용하지 않음
            .and().cors()
            .and().authorizeRequests() // 요청에 대한 사용권한 체크
            .antMatchers("/me").hasRole("USER")
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, userApplication),
                UsernamePasswordAuthenticationFilter.class);
    }
}