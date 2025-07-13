package com.bookstore.mapper;

import com.bookstore.domain.User;
import com.bookstore.dto.requestDTO.UserRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    // 사용자 조회
    User findById(@Param("id") String id);
    User findByUserId(@Param("userId") Long userId);
    User findByEmail(@Param("email") String email);
    User findByOauthProviderAndUid(@Param("oauthProvider") String oauthProvider, @Param("oauthProviderUid") String oauthProviderUid);
    
    // 사용자 생성
    int insertUser(User user);
    
    // 사용자 수정
    int updateUser(User user);
    
    // 사용자 상태 변경
    int updateUserStatus(@Param("userId") Long userId, @Param("statusCode") String statusCode);
    
    // 마지막 로그인 시간 업데이트
    int updateLastLoginAt(@Param("userId") Long userId);
    
    // ID 중복 확인
    boolean existsById(@Param("id") String id);
    
    // 이메일 중복 확인
    boolean existsByEmail(@Param("email") String email);
    
    // 관리자용 사용자 목록 조회
    List<User> findAllUsers();

    String selectWriterName(UserRequestDTO dto);
}
