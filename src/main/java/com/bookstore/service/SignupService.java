package com.bookstore.service;

import com.bookstore.domain.SignupSession;
import com.bookstore.domain.User;
import com.bookstore.dto.requestDTO.SignupRequestDTO;
import com.bookstore.exception.CustomException;
import com.bookstore.exception.ErrorCode;
import com.bookstore.mapper.SignupSessionMapper;
import com.bookstore.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {
    
    private final UserMapper userMapper;
    private final SignupSessionMapper signupSessionMapper;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 회원가입 세션 생성 (UC01)
     */
    public String createSignupSession() {
        String sessionKey = UUID.randomUUID().toString();
        
        SignupSession session = SignupSession.builder()
                .sessionKey(sessionKey)
                .signupStep(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusHours(24)) // 24시간 후 만료
                .build();
        
        signupSessionMapper.insertSignupSession(session);
        return sessionKey;
    }
    
    /**
     * 회원가입 세션 조회
     */
    public SignupSession getSignupSession(String sessionKey) {
        return signupSessionMapper.findBySessionKey(sessionKey);
    }
    
    /**
     * 회원가입 단계 업데이트
     */
    public void updateSignupStep(String sessionKey, Integer step) {
        signupSessionMapper.updateSignupStep(sessionKey, step);
    }
    
    /**
     * ID 중복 확인 (UC09)
     */
    public boolean checkIdDuplicate(String id) {
        return userMapper.existsById(id);
    }
    
    /**
     * 이메일 중복 확인
     */
    public boolean checkEmailDuplicate(String email) {
        return userMapper.existsByEmail(email);
    }
    
    /**
     * 회원가입 완료 (UC09, UC10)
     */
    public User completeSignup(SignupRequestDTO signupRequest, String sessionKey) {
        // 1. 세션 확인
        SignupSession session = signupSessionMapper.findBySessionKey(sessionKey);
        if (session == null || session.getSignupStep() < 4) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "회원가입 단계가 올바르지 않습니다.");
        }
        
        // 2. ID 중복 확인
        if (userMapper.existsById(signupRequest.getId())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 사용 중인 ID입니다.");
        }
        
        // 3. 이메일 중복 확인
        if (signupRequest.getEmail() != null && userMapper.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 사용 중인 이메일입니다.");
        }
        
        // 4. 비밀번호 확인
        if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호가 일치하지 않습니다.");
        }
        
        // 5. 사용자 생성
        User user = User.builder()
                        .id(signupRequest.getId())
                        .password(passwordEncoder.encode(signupRequest.getPassword()))
                        .name(signupRequest.getName())
                        .email(signupRequest.getEmail())
                        .phone(signupRequest.getPhone())
                        .address(signupRequest.getAddress())
                        .oauthProvider(signupRequest.getOauthProvider())
                        .oauthProviderUid(signupRequest.getOauthProviderUid())
                        .statusCode("ACTIVE")
                        .roleCode("USER")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
        
        userMapper.insertUser(user);
        
        // 6. 세션 삭제
        signupSessionMapper.deleteSignupSession(sessionKey);
        
        return user;
    }
    
    /**
     * OAuth 회원가입 (UC07)
     */
    public User oauthSignup(String oauthProvider, String oauthProviderUid, String name, String email) {
        // 1. OAuth 계정 중복 확인
        User existingUser = userMapper.findByOauthProviderAndUid(oauthProvider, oauthProviderUid);
        if (existingUser != null) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 가입된 OAuth 계정입니다.");
        }
        
        // 2. 이메일 중복 확인
        if (email != null && userMapper.existsByEmail(email)) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 사용 중인 이메일입니다.");
        }
        
        // 3. OAuth 사용자 생성
        User user = User.builder()
                .id("oauth_" + oauthProvider + "_" + oauthProviderUid) // 임시 ID
                .password("") // OAuth는 비밀번호 없음
                .name(name)
                .email(email)
                .oauthProvider(oauthProvider)
                .oauthProviderUid(oauthProviderUid)
                .statusCode("ACTIVE")
                .roleCode("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        userMapper.insertUser(user);
        return user;
    }
    
    /**
     * 만료된 세션 정리
     */
    public void cleanupExpiredSessions() {
        signupSessionMapper.deleteExpiredSessions();
    }
} 