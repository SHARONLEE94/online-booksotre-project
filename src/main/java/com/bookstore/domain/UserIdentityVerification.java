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
public class UserIdentityVerification {
    
    private Long verificationId;
    private Long userId;                  // 사용자 ID (기존 계정 연동 시)
    private String verificationType;      // 인증 타입 (PHONE, IPIN)
    private String statusCode;            // 상태 (PENDING, SUCCESS, FAIL)
    private String errorCode;             // 오류 코드
    private LocalDateTime requestAt;      // 요청 시간
    private LocalDateTime responseAt;     // 응답 시간
    private LocalDateTime createdAt;      // 생성일
    private LocalDateTime updatedAt;      // 수정일
} 