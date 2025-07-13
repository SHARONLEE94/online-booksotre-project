package com.bookstore.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private T data;
    private ErrorResponse error;
    private LocalDateTime timestamp;
    
    @Builder
    public ApiResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
    
    // 성공 응답 생성
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                            .success(true)
                            .data(data)
                            .build();
    }
    
    // 성공 응답 (데이터 없음)
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                            .success(true)
                            .build();
    }
    
    // 실패 응답 생성
    public static <T> ApiResponse<T> error(@NotNull ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                            .success(false)
                            .error(ErrorResponse.builder()
                            .code(errorCode.getCode())
                            .message(errorCode.getMessage())
                            .status(errorCode.getStatus())
                            .build())
                             .build();
    }
    
    // 실패 응답 생성 (커스텀 메시지)
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage) {
        return ApiResponse.<T>builder()
                        .success(false)
                        .error(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .message(customMessage)
                        .status(errorCode.getStatus())
                        .build())
                         .build();
    }
    
    @Getter
    @Builder
    public static class ErrorResponse {
        private String code;
        private String message;
        private int status;
    }
} 