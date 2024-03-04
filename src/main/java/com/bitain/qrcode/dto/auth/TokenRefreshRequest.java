package com.bitain.qrcode.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenRefreshRequest {
    private String refreshToken;
}
