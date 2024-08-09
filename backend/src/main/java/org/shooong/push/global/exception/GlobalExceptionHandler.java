package org.shooong.push.global.exception;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> error = Map.of("error", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        Map<String, String> error = Map.of("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CustomJWTException.class)
    public ResponseEntity<?> handleJWTException(CustomJWTException e) {
        String message = e.getMessage();

        if ("Expired".equals(message)) {
            message = "ERROR_ACCESS_TOKEN";
        } else {
            message = "INVALID_ACCESS_TOKEN";
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new Gson().toJson(Map.of("error", message)));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicateCouponException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCouponException(
        DuplicateCouponException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getHttpStatus().value(),
            ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    //
    @ExceptionHandler(CouponLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleCouponLimitExceededException(CouponLimitExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getHttpStatus().value(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(CouponNotInPeriodException.class)
    public ResponseEntity<ErrorResponse> handleCouponNotInPeriodException(CouponNotInPeriodException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getHttpStatus().value(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
    public static class ErrorResponse {

        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

}
