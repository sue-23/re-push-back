package org.shooong.push.global.exception;


import lombok.Getter;

@Getter
public class CouponConditionNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    public CouponConditionNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
