package com.bitain.qrcode.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ComponentException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public ComponentException(String message, HttpStatus status){
        super(message);
        this.status = status;
        this.message = message;
    }
}
