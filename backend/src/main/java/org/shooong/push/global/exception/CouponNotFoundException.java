package org.shooong.push.global.exception;

public class CouponNotFoundException extends GlobalException{

    public CouponNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
