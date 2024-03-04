package com.bitain.qrcode.service;

import com.bitain.qrcode.entity.RefreshTokenEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface IRefreshTokenService {
    String getRefreshTokenFromCookies(HttpServletRequest servletRequest);
    ResponseCookie generateRefreshTokenCookie(String email);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
    ResponseCookie clearRefreshTokenCookie();
    void deleteByUserId(Long userId);
}
