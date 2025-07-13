package com.bookstore.dto.requestDTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    
    private String id;
    
    private String password;
    
    private boolean rememberId;  // 아이디 기억 여부
} 