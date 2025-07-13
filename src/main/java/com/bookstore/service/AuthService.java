package com.bookstore.service;

import com.bookstore.domain.User;
import com.bookstore.dto.requestDTO.LoginRequestDTO;
import com.bookstore.dto.responseDTO.LoginResponseDTO;
import com.bookstore.exception.CustomException;
import com.bookstore.exception.ErrorCode;
import com.bookstore.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 로그인 처리
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest, HttpSession session) {
        // 1. 사용자 조회
        User user = userMapper.findById(loginRequest.getId());
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 2. 계정 상태 확인
        if (!"ACTIVE".equals(user.getStatusCode())) {
            if ("SUSPENDED".equals(user.getStatusCode())) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND, "비활성화된 계정입니다.");
            } else if ("DORMANT".equals(user.getStatusCode())) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND, "휴면 계정입니다. 휴면 해제 절차를 진행해주세요.");
            }
        }
        
        // 3. 비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        
        // 4. 마지막 로그인 시간 업데이트
        userMapper.updateLastLoginAt(user.getUserId());
        
        // 5. 세션에 사용자 정보 저장
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userRole", user.getRoleCode());
        session.setAttribute("userName", user.getName());
        
        // 6. 응답 생성
        return LoginResponseDTO.builder()
                .success(true)
                .message("로그인에 성공했습니다.")
                .token(session.getId())
                .userInfo(LoginResponseDTO.UserInfo.builder()
                        .userId(user.getUserId())
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roleCode(user.getRoleCode())
                        .statusCode(user.getStatusCode())
                        .build())
                .build();
    }
    
    /**
     * 로그아웃 처리
     */
    public void logout(HttpSession session) {
        session.invalidate();
    }
    
    /**
     * OAuth 로그인 처리
     */
    public LoginResponseDTO oauthLogin(String oauthProvider, String oauthProviderUid, HttpSession session) {
        // 1. OAuth 계정으로 사용자 조회
        User user = userMapper.findByOauthProviderAndUid(oauthProvider, oauthProviderUid);
        
        if (user == null) {
            // 신규 OAuth 사용자 - 회원가입 필요
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "OAuth 계정으로 가입된 사용자가 없습니다.");
        }
        
        // 2. 계정 상태 확인
        if (!"ACTIVE".equals(user.getStatusCode())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "비활성화된 계정입니다.");
        }
        
        // 3. 마지막 로그인 시간 업데이트
        userMapper.updateLastLoginAt(user.getUserId());
        
        // 4. 세션에 사용자 정보 저장
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userRole", user.getRoleCode());
        session.setAttribute("userName", user.getName());
        
        // 5. 응답 생성
        return LoginResponseDTO.builder()
                .success(true)
                .message("OAuth 로그인에 성공했습니다.")
                .token(session.getId())
                .userInfo(LoginResponseDTO.UserInfo.builder()
                        .userId(user.getUserId())
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roleCode(user.getRoleCode())
                        .statusCode(user.getStatusCode())
                        .build())
                .build();
    }
    
    /**
     * 현재 로그인한 사용자 정보 조회
     */
    public User getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return userMapper.findByUserId(userId);
    }
    
    /**
     * 로그인 상태 확인
     */
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }
} 