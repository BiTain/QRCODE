package com.bitain.qrcode.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorDetails> handleException(Exception exception, WebRequest request, HttpStatus status){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(true))
                .errorCode(String.valueOf(status.value()))
                .build();
        return new ResponseEntity<>(errorDetails,status);
    }

    @ExceptionHandler(ComponentException.class)
    public ResponseEntity<ErrorDetails> handleComponentException(ComponentException exception,WebRequest request){
        return handleException(exception,request,exception.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(RuntimeException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDetails> handleJwtException(RuntimeException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({TokenRefreshException.class, DisableAccountException.class})
    public ResponseEntity<ErrorDetails> handleTokenRefreshException(RuntimeException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorDetails> handleConflictException(RuntimeException exception, WebRequest webRequest){
        return handleException(exception,webRequest,HttpStatus.CONFLICT);
    }
}
