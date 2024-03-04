package com.bitain.qrcode.service;

import com.bitain.qrcode.dto.auth.AuthRespone;
import com.bitain.qrcode.dto.auth.TokenRefreshResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface IAuthService {
    AuthRespone authenticateAndGenerateToken(String userName, String passWord);
    TokenRefreshResponse processRefreshToken(HttpServletRequest request);
    ResponseCookie doLogout(Long userId);
}
