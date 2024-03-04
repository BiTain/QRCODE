package com.bitain.qrcode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceConflictException extends RuntimeException{
    public ResourceConflictException (String value, String message){
        super(String.format("Failed for [%s]: %s", value, message));
    }
}
