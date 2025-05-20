package com.bohemio.todoreactspringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSignUpRequest(
        @NotBlank(message = "사용자 이름은 필수 입력 항목입니다")
        @Size(min = 3, max = 20, message = "사용자 이름은 3자 이상, 20자 이하로 입력해주세요!")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 6, max = 40, message = "비밀번호는 6자 이상 40자 이하로 입력해주세요.")
        String password,

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "이메일 주소는 최대 50자까지 가능합니다.")
        String email
) {
}
