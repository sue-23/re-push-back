package org.shooong.push.global.exception;

// 쿠폰 발급 수량 초과 예외
public class CouponLimitExceededException extends GlobalException{

    public CouponLimitExceededException(ErrorCode errorCode) {
        super(errorCode);
    }
}
