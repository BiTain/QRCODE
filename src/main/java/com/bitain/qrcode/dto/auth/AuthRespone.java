package com.bitain.qrcode.dto.auth;

import lombok.*;
import org.springframework.http.ResponseCookie;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRespone {
    private String accessToken;
    private String type = "Bearer";
    private ResponseCookie refreshToken;
}
