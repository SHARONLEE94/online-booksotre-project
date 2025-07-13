package com.bookstore.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // 공통 에러
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류가 발생했습니다."),
    
    // 사용자 관련 에러
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(409, "U002", "이미 존재하는 사용자입니다."),
    INVALID_PASSWORD(400, "U003", "잘못된 비밀번호입니다."),
    UNAUTHORIZED_ACCESS(401, "U004", "인증이 필요합니다."),
    
    // 도서 관련 에러
    BOOK_NOT_FOUND(404, "B001", "도서를 찾을 수 없습니다."),
    BOOK_OUT_OF_STOCK(400, "B002", "재고가 부족합니다."),
    
    // 주문 관련 에러
    ORDER_NOT_FOUND(404, "O001", "주문을 찾을 수 없습니다."),
    ORDER_ALREADY_PROCESSED(400, "O002", "이미 처리된 주문입니다."),
    
    // 장바구니 관련 에러
    CART_ITEM_NOT_FOUND(404, "CI001", "장바구니 상품을 찾을 수 없습니다."),
    CART_EMPTY(400, "CI002", "장바구니가 비어있습니다."),
    
    // 결제 관련 에러
    PAYMENT_FAILED(400, "P001", "결제에 실패했습니다."),
    INSUFFICIENT_BALANCE(400, "P002", "잔액이 부족합니다."),
    
    // 리뷰 관련 에러
    REVIEW_NOT_FOUND(404, "R001", "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(409, "R002", "이미 작성된 리뷰입니다.");
    
    private final int status;
    private final String code;
    private final String message;
} 