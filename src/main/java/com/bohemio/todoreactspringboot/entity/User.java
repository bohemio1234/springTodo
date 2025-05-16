package com.bohemio.todoreactspringboot.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="app_users")
public class User implements UserDetails {


    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 20)
    private String role;

    public User() {
    }

    public User(long id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 현재는 role 필드에 "ROLE_USER" 또는 "ROLE_ADMIN" 같은 단일 문자열 역할만 저장한다고 가정.
        // 이 문자열을 SimpleGrantedAuthority 객체로 만들어서 컬렉션에 담아 반환합니다.
        // 만약 여러 역할을 콤마(,) 등으로 구분해서 role 필드에 저장한다면, 여기서 파싱해서 여러 GrantedAuthority를 만들어야 합니다.
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료되지 않음 (항상 true 또는 DB에 관련 필드 추가)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠기지 않음 (항상 true 또는 DB에 관련 필드 추가)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료되지 않음 (항상 true 또는 DB에 관련 필드 추가)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화됨 (항상 true 또는 DB에 관련 필드 추가)
    }

}
