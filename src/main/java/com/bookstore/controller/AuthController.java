package com.bookstore.controller;

import com.bookstore.dto.requestDTO.LoginRequestDTO;
import com.bookstore.dto.responseDTO.LoginResponseDTO;
import com.bookstore.exception.ApiResponse;
import com.bookstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 로그인 화면 (UC13)
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    
    /**
     * 로그인 처리 (UC13)
     */
    @PostMapping("/api/login")
    @ResponseBody
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest, HttpSession session) {
        try {
            LoginResponseDTO response = authService.login(loginRequest, session);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ApiResponse.error(com.bookstore.exception.ErrorCode.INVALID_PASSWORD, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
    
    /**
     * 로그아웃 처리
     */
    @PostMapping("/api/logout")
    @ResponseBody
    public ApiResponse<String> logout(HttpSession session) {
        authService.logout(session);
        return ApiResponse.success("로그아웃되었습니다.");
    }
    
    /**
     * OAuth 로그인 처리 (UC07)
     */
    @GetMapping("/oauth/{provider}/callback")
    public String oauthCallback(@PathVariable String provider, 
                               @RequestParam String code, 
                               HttpSession session, 
                               Model model) {
        try {
            // OAuth 인증 코드로 사용자 정보 조회 (실제 구현에서는 OAuth API 호출)
            // 여기서는 예시로 하드코딩
            String oauthProviderUid = "sample_uid_" + System.currentTimeMillis();
            
            LoginResponseDTO response = authService.oauthLogin(provider, oauthProviderUid, session);
            model.addAttribute("loginSuccess", true);
            model.addAttribute("userName", response.getUserInfo().getName());
            
            return "redirect:/main";
        } catch (Exception e) {
            log.error("OAuth login failed: {}", e.getMessage());
            model.addAttribute("error", "OAuth 로그인에 실패했습니다.");
            return "auth/login";
        }
    }
    
    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/api/user/current")
    @ResponseBody
    public ApiResponse<Object> getCurrentUser(HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return ApiResponse.error(com.bookstore.exception.ErrorCode.UNAUTHORIZED_ACCESS);
        }
        
        return ApiResponse.success(authService.getCurrentUser(session));
    }
} 