package com.bitain.qrcode.dto.auth;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshResponse {
    private String newAccessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    public TokenRefreshResponse(String newAccessToken, String refreshToken) {
        this.newAccessToken = newAccessToken;
        this.refreshToken = refreshToken;
    }
}
