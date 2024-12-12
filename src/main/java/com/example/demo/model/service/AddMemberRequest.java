package com.example.demo.model.service;

import lombok.*; // 어노테이션 자동 생성
import com.example.demo.model.domain.Member;

import jakarta.validation.constraints.*;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddMemberRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "이름은 특수문자를 포함할 수 없습니다.")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z]).{8,}")
    private String password;

    @NotBlank
    @Min(value = 19, message = "나이는 19세 이상이어야 합니다.") // 나이 최소 19
    @Max(value = 90, message = "나이는 90세 이하이어야 합니다.")
    private String age;

    private String mobile;
    
    private String address;
    
    public Member toEntity(){ // Member 생성자를 통해 객체 생성
        return Member.builder()
            .name(name)
            .email(email)
            .password(password)
            .age(age)
            .mobile(mobile)
            .address(address)
            .build();
    }
}