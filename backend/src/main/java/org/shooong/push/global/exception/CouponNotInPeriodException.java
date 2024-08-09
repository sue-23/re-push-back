package org.shooong.push.global.exception;


// 쿠폰 발급 기간 예외
public class CouponNotInPeriodException extends GlobalException{

    public CouponNotInPeriodException(ErrorCode errorCode) {
        super(errorCode);
    }
}
