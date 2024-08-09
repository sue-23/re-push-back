package org.shooong.push.global.exception;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}