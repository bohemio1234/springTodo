package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.dto.JwtResponse;
import com.bohemio.todoreactspringboot.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.stream.Collectors;

public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username()
        , loginRequest.password());
        //UsernamePasswordAuthenticationToken은 인증되지 않은 토큰.
        //일단 유저네임, 비번으로 토큰 만드는거.
        //여긴 권한이 없음. 당연히

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //요 메서드가 호출이되면 SecurityConfig에 우리가 등록한 UserDetailsService에게 이 username잇는놈찾아줘!
        //그 해당 사용자 찾아서 그 정보들을 가져와.
        //그리고 userdetails의 암호화된 비번과 저 평문 비번과 비교한뒤
        //일치하면 Authentication객체 생성. 거기엔 권한이 잇겟지.

        String username = authentication.getName();
        Instant now = Instant.now();// 토큰 발급 시간과 만료 시간을 설정합니다.
        Instant expiry = now.plusMillis(jwtExpirationMs);

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect( Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(username)
                .claim("scope",scope)
                .build();

        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new JwtResponse(token);
    }

}
