package com.bookstore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalViewExceptionHandler {
    
    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {
        log.error("CustomException: {}", e.getMessage());
        
        model.addAttribute("errorCode", e.getErrorCode().getCode());
        model.addAttribute("errorMessage", e.getErrorCode().getMessage());
        model.addAttribute("status", e.getErrorCode().getStatus());
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        
        return "error/error"; // error.jsp 페이지로 이동
    }
    
    /**
     * 404 에러 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException e, Model model) {
        log.error("NoHandlerFoundException: {}", e.getMessage());
        
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", "요청하신 페이지를 찾을 수 없습니다.");
        model.addAttribute("status", 404);
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        
        return "error/404";
    }
    
    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("Unexpected error occurred: ", e);
        
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
        model.addAttribute("status", 500);
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        
        return "error/error";
    }
} 