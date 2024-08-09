package org.shooong.push.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송지가 존재하지 않습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 주문입니다."),
    COUPON_LIMIT_ISSUE(HttpStatus.NOT_FOUND, "쿠폰 수량이 모두 소진되었습니다."),
    ISSUANCE_DUPLICATE_COUPON(HttpStatus.CONFLICT, "이미 쿠폰을 발급 받았습니다."),
    COUPON_NOT_FOUND_CONDITION(HttpStatus.NOT_FOUND, "쿠폰 발급 조건을 찾을 수 없습니다."),
    COUPON_NOT_IN_PERIOD(HttpStatus.BAD_REQUEST, "발급 기간이 아닙니다.");







    private final HttpStatus httpStatus;
    private final String message;


}
