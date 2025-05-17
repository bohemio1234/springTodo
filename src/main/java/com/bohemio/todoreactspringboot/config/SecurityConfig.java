package com.bohemio.todoreactspringboot.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.keystore.location}")
    private String keystoreLocation;

    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    @Value("${jwt.key.alias}")
    private String keyAlias;

    @Value("${jwt.key.password}")
    private String keyPassword;

//    @Bean
//    public KeyPair keyPair() {
//        try {
//            KeyStore keyStore = KeyStore.getInstance("JKS");
//            // ClassPathResource를 사용해서 src/main/resources 폴더의 keystore 파일을 InputStream으로 읽어옵니다.
//            InputStream resourceAsStream = new ClassPathResource(keystoreLocation).getInputStream();
//            // KeyStore를 로드합니다. (keystore 파일, keystore 비밀번호 필요)
//            keyStore.load(resourceAsStream, keystorePassword.toCharArray());
//
//            // KeyStore에서 별칭(alias)과 키 비밀번호를 사용해서 개인키(PrivateKey)를 가져옵니다.
//            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
//            // KeyStore에서 별칭(alias)을 사용해서 인증서(Certificate)를 가져옵니다.
//            Certificate certificate = keyStore.getCertificate(keyAlias);
//            // 인증서에서 공개키(PublicKey)를 가져옵니다.
//            PublicKey publicKey = certificate.getPublicKey();
//
//            // 가져온 공개키와 개인키로 KeyPair 객체를 생성하여 반환합니다.
//            if (publicKey != null && privateKey != null) {
//                return new KeyPair(publicKey, privateKey);
//            } else {
//                throw new IllegalStateException(keyAlias + "에 해당하는 키 쌍을 키스토어에서 찾을 수 없습니다.");
//            }
//        } catch (Exception e) {
//            // 키 로딩 중 문제 발생 시 예외 처리
//            throw new IllegalStateException("키스토어에서 RSA 키 쌍을 로드하는 데 실패했습니다. 경로: " + keystoreLocation, e);
//        }
//
//    }

    @Bean
    public KeyPair keyPair() {
        try {
            // "RSA" 알고리즘을 사용하는 KeyPairGenerator 인스턴스를 가져옵니다.
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // 키의 크기를 2048비트로 초기화합니다. (일반적으로 2048 이상 권장)
            keyPairGenerator.initialize(2048);
            // 새로운 KeyPair(공개키 + 개인키)를 생성하여 반환합니다.
            // 이 KeyPair는 애플리케이션이 시작될 때 한 번 생성되어 메모리에 유지됩니다.
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            // RSA 알고리즘을 지원하지 않는 심각한 문제 발생 시 예외 처리
            throw new IllegalStateException("애플리케이션 시작 시 RSA 키 쌍 생성에 실패했습니다: RSA 알고리즘을 찾을 수 없습니다.", ex);
        }
    }

    @Bean
    public RSAKey rsakey(KeyPair keyPair){
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsakey) {
        JWKSet jwkSet = new JWKSet(rsakey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws Exception {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaKey.toPublicKey()).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("testuser")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build()  ;
//
//        UserDetails admin = User.builder()
//                .username("adminuser")
//                .password(passwordEncoder().encode("adminpass"))
//                .roles("ADMIN", "USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // "/authenticate" 경로는 인증 없이 모두 허용
                        .requestMatchers( HttpMethod.GET, "/users/{username}/todos", "/users/{username}/todos/{id}").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers( HttpMethod.POST, "/users/{username}/todos").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{username}/todos/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{username}/todos/{id}").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")


                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter))
                );


        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

            }
        };
    }



}


