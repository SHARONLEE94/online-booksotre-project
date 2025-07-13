package com.bookstore.dto.requestDTO;

import lombok.Data;

@Data
public class SignupRequestDTO {
    
    private String id;                    // 로그인 ID
    private String password;              // 비밀번호
    private String passwordConfirm;       // 비밀번호 확인
    private String name;                  // 사용자 이름
    private String email;                 // 이메일
    private String phone;                 // 연락처
    private String address;               // 주소
    private String[] agreedTerms;         // 동의한 약관 ID 배열
    private String oauthProvider;         // OAuth 제공자
    private String oauthProviderUid;      // OAuth 제공자의 사용자 ID
} 