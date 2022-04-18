package com.news.dev.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private String secretKey = "DEV_NEWS";
    private final long tokenExpiration = 30 * 60 * 1000L; // 토큰 유효기간 (30분)

    // Bean
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() { // 객체 초기화, secretKey를 base64로 인코딩
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt Create
    public String createToken(String email) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now) // 지금 시간부터 30분 Set
                .setExpiration(new Date(now.getTime() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Jwt 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUser(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Token으로 회원 정보 조회
    public String getUser(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 Token을 가져오기 - "X-AUTH-TOKEN" : "TOKEN"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // Token의 유효성 + 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
