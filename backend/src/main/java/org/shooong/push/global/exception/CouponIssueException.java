package org.shooong.push.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

class CouponIssueException extends RuntimeException {

    public CouponIssueException(String message) {
        super(message);
    }
}