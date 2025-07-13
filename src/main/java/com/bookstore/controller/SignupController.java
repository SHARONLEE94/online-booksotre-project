package com.bookstore.controller;

import com.bookstore.domain.SignupSession;
import com.bookstore.domain.User;
import com.bookstore.dto.requestDTO.SignupRequestDTO;
import com.bookstore.exception.ApiResponse;
import com.bookstore.service.AuthService;
import com.bookstore.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignupController {
    
    private final SignupService signupService;
    private final AuthService authService;
    
    /**
     * 회원가입 화면 진입 (UC01)
     */
    @GetMapping("/join")
    public String joinPage(HttpSession session, Model model) {
        // 세션 키 생성
        String sessionKey = signupService.createSignupSession();
        session.setAttribute("signupSessionKey", sessionKey);
        
        model.addAttribute("sessionKey", sessionKey);
        return "auth/join";
    }
    
    /**
     * 본인인증 선택 화면 (UC02)
     */
    @GetMapping("/join/verify/self")
    public String verifySelfPage(HttpSession session, Model model) {
        String sessionKey = (String) session.getAttribute("signupSessionKey");
        if (sessionKey == null) {
            return "redirect:/join";
        }
        
        signupService.updateSignupStep(sessionKey, 1);
        model.addAttribute("sessionKey", sessionKey);
        return "auth/verify-self";
    }
    
    /**
     * 본인인증 요청 (UC03)
     */
    @PostMapping("/api/join/verify/request")
    @ResponseBody
    public ApiResponse<String> requestVerification(@RequestParam String verificationType, 
                                                 HttpSession session) {
        try {
            String sessionKey = (String) session.getAttribute("signupSessionKey");
            if (sessionKey == null) {
                return ApiResponse.error(com.bookstore.exception.ErrorCode.INVALID_INPUT_VALUE, "세션이 만료되었습니다.");
            }
            
            // 실제로는 외부 인증 API 호출
            // 여기서는 성공으로 가정
            signupService.updateSignupStep(sessionKey, 2);
            
            return ApiResponse.success("인증 요청이 완료되었습니다.");
        } catch (Exception e) {
            log.error("Verification request failed: {}", e.getMessage());
            return ApiResponse.error(com.bookstore.exception.ErrorCode.INTERNAL_SERVER_ERROR, "인증 요청에 실패했습니다.");
        }
    }
    
    /**
     * 본인인증 완료 확인 (UC04)
     */
    @PostMapping("/api/join/verify/complete")
    @ResponseBody
    public ApiResponse<String> completeVerification(HttpSession session) {
        try {
            String sessionKey = (String) session.getAttribute("signupSessionKey");
            if (sessionKey == null) {
                return ApiResponse.error(com.bookstore.exception.ErrorCode.INVALID_INPUT_VALUE, "세션이 만료되었습니다.");
            }
            
            signupService.updateSignupStep(sessionKey, 3);
            return ApiResponse.success("인증이 완료되었습니다.");
        } catch (Exception e) {
            log.error("Verification completion failed: {}", e.getMessage());
            return ApiResponse.error(com.bookstore.exception.ErrorCode.INTERNAL_SERVER_ERROR, "인증 완료 처리에 실패했습니다.");
        }
    }
    
    /**
     * 약관동의 화면 (UC08)
     */
    @GetMapping("/join/terms")
    public String termsPage(HttpSession session, Model model) {
        String sessionKey = (String) session.getAttribute("signupSessionKey");
        if (sessionKey == null) {
            return "redirect:/join";
        }
        
        SignupSession sessionInfo = signupService.getSignupSession(sessionKey);
        if (sessionInfo == null || sessionInfo.getSignupStep() < 3) {
            return "redirect:/join";
        }
        
        model.addAttribute("sessionKey", sessionKey);
        return "auth/terms";
    }
    
    /**
     * 약관동의 처리 (UC08)
     */
    @PostMapping("/api/join/terms")
    @ResponseBody
    public ApiResponse<String> agreeTerms(@RequestParam String[] termsIds, HttpSession session) {
        try {
            String sessionKey = (String) session.getAttribute("signupSessionKey");
            if (sessionKey == null) {
                return ApiResponse.error(com.bookstore.exception.ErrorCode.INVALID_INPUT_VALUE, "세션이 만료되었습니다.");
            }
            
            if (termsIds.length < 2) {
                return ApiResponse.error(com.bookstore.exception.ErrorCode.INVALID_INPUT_VALUE, "필수 약관에 동의해주세요.");
            }
            
            signupService.updateSignupStep(sessionKey, 4);
            return ApiResponse.success("약관 동의가 완료되었습니다.");
        } catch (Exception e) {
            log.error("Terms agreement failed: {}", e.getMessage());
            return ApiResponse.error(com.bookstore.exception.ErrorCode.INTERNAL_SERVER_ERROR, "약관 동의 처리에 실패했습니다.");
        }
    }
    
    /**
     * 개인정보 입력 화면 (UC09)
     */
    @GetMapping("/join/info")
    public String infoPage(HttpSession session, Model model) {
        String sessionKey = (String) session.getAttribute("signupSessionKey");
        if (sessionKey == null) {
            return "redirect:/join";
        }
        
        SignupSession sessionInfo = signupService.getSignupSession(sessionKey);
        if (sessionInfo == null || sessionInfo.getSignupStep() < 4) {
            return "redirect:/join";
        }
        
        model.addAttribute("sessionKey", sessionKey);
        return "auth/info";
    }
    
    /**
     * ID 중복확인 (UC09)
     */
    @GetMapping("/api/join/check-id")
    @ResponseBody
    public ApiResponse<Boolean> checkIdDuplicate(@RequestParam String id) {
        try {
            boolean isDuplicate = signupService.checkIdDuplicate(id);
            return ApiResponse.success(!isDuplicate);
        } catch (Exception e) {
            log.error("ID duplicate check failed: {}", e.getMessage());
            return ApiResponse.error(com.bookstore.exception.ErrorCode.INTERNAL_SERVER_ERROR, "ID 중복 확인에 실패했습니다.");
        }
    }
    
    /**
     * 회원가입 완료 (UC09, UC10)
     */
    @PostMapping("/api/join/complete")
    @ResponseBody
    public ApiResponse<User> completeSignup(@RequestBody SignupRequestDTO signupRequest, HttpSession session) {
        try {
            String sessionKey = (String) session.getAttribute("signupSessionKey");
            if (sessionKey == null) {
                return ApiResponse.error(com.bookstore.exception.ErrorCode.INVALID_INPUT_VALUE, "세션이 만료되었습니다.");
            }
            
            User user = signupService.completeSignup(signupRequest, sessionKey);
            session.removeAttribute("signupSessionKey");
            
            return ApiResponse.success(user);
        } catch (Exception e) {
            log.error("Signup completion failed: {}", e.getMessage());
            return ApiResponse.error(com.bookstore.exception.ErrorCode.INTERNAL_SERVER_ERROR, "회원가입 처리에 실패했습니다.");
        }
    }
    
    /**
     * 가입 완료 화면 (UC10)
     */
    @GetMapping("/join/complete")
    public String completePage(@RequestParam String userName, Model model) {
        model.addAttribute("userName", userName);
        return "auth/complete";
    }
} 