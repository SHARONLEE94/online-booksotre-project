package com.bookstore.mapper;

import com.bookstore.domain.SignupSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SignupSessionMapper {
    
    // 세션 생성
    int insertSignupSession(SignupSession session);
    
    // 세션 조회
    SignupSession findBySessionKey(@Param("sessionKey") String sessionKey);
    
    // 세션 단계 업데이트
    int updateSignupStep(@Param("sessionKey") String sessionKey, @Param("signupStep") Integer signupStep);
    
    // 인증 정보 업데이트
    int updateVerificationInfo(@Param("sessionKey") String sessionKey, 
                              @Param("verificationType") String verificationType,
                              @Param("verificationCode") String verificationCode,
                              @Param("verificationExpiredAt") String verificationExpiredAt);
    
    // 세션 삭제
    int deleteSignupSession(@Param("sessionKey") String sessionKey);
    
    // 만료된 세션 정리
    int deleteExpiredSessions();
} 