package com.test.test.exeption;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.test.test.dto.ErrorResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@PropertySource("classpath:error-codes.properties")
public class GlobalExceptionHandler {

    private final Environment env;

    public GlobalExceptionHandler(Environment env) {
        this.env = env;
    }

    private String getErrorCode(String key) {
        return env.getProperty(key, "E000"); // کد پیش‌فرض اگر کلید پیدا نشد
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(" | "));
        ErrorResponse error = new ErrorResponse(400, getErrorCode("validation.error"), message);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(SecurityException ex) {
        ErrorResponse error = new ErrorResponse(401, getErrorCode("unauthorized.error"), "توکن نامعتبر است. لطفاً دوباره وارد شوید.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(403, getErrorCode("forbidden.error"), "دسترسی ممنوع است.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        String errorCode = getErrorCode(ex.getErrorKey());
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(),
                errorCode,
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnum(HttpMessageNotReadableException ex) {
        String message = "مقدار وارد شده صحیح نیست.";
        if (ex.getCause() instanceof InvalidFormatException cause &&
                cause.getTargetType().isEnum()) {
            message = "مقدار وارد شده صحیح نیست.";
        }
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                getErrorCode("enum.invalid"),
                message
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse(500, getErrorCode("internal.error"), "خطای غیرمنتظره‌ای رخ داده است.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
