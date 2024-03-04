package com.bitain.qrcode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
    private Integer code;
    private String message;
}
