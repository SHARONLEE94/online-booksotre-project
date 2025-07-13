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
public class SignupSession {
    
    private Long sessionId;
    private String sessionKey;            // 세션 키 (UUID)
    private Integer signupStep;           // 회원가입 단계 (0~4)
    private String verificationType;      // 인증 타입 (PHONE, IPIN)
    private String verificationCode;      // 인증 코드
    private LocalDateTime verificationExpiredAt; // 인증 만료 시간
    private LocalDateTime createdAt;      // 생성일
    private LocalDateTime updatedAt;      // 수정일
    private LocalDateTime expiredAt;      // 세션 만료일
} 