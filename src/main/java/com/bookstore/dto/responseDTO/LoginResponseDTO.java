package com.bookstore.dto.responseDTO;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class LoginResponseDTO {
    
    private boolean success;
    private String message;
    private String token;                 // JWT 토큰 또는 세션 ID
    private UserInfo userInfo;            // 사용자 정보
    
    @Data
    @Builder
    public static class UserInfo {
        private Long userId;
        private String id;
        private String name;
        private String email;
        private String roleCode;
        private String statusCode;
    }
} 