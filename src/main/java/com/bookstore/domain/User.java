package com.bookstore.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long userId;
    private String id;                    // 로그인 ID
    private String password;              // 암호화된 비밀번호
    private String name;                  // 사용자 이름
    private String email;                 // 이메일
    private String phone;                 // 연락처
    private String address;               // 주소
    private String oauthProvider;         // OAuth 제공자 (NAVER, KAKAO, GOOGLE)
    private String oauthProviderUid;      // OAuth 제공자의 사용자 ID
    private String statusCode;            // 계정 상태 (ACTIVE, SUSPENDED, DORMANT)
    private String roleCode;              // 권한 (USER, ADMIN)
    private LocalDateTime createdAt;      // 가입일
    private LocalDateTime updatedAt;      // 수정일
    private LocalDateTime lastLoginAt;    // 마지막 로그인 시간
} 