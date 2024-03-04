package com.bitain.qrcode.service.imp;

import com.bitain.qrcode.constants.AppConstants;
import com.bitain.qrcode.entity.RefreshTokenEntity;
import com.bitain.qrcode.exception.TokenRefreshException;
import com.bitain.qrcode.repository.RefreshTokenRepository;
import com.bitain.qrcode.repository.UserRepository;
import com.bitain.qrcode.service.IRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${security.jwt.jwtRefreshExpiration-time}")
    private Long refreshTokenDurationMs;
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }
    private Logger logger = Logger.getLogger(RefreshTokenService.class.getName());
    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest servletRequest) {
        Cookie cookie = WebUtils.getCookie(servletRequest, AppConstants.NAME_REFRESH_TOKEN_COOKIE);
        return cookie != null ? cookie.getValue() : null;
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String email) {
        RefreshTokenEntity refreshToken = this.saveRefreshToken(email);
        return ResponseCookie.from(AppConstants.NAME_REFRESH_TOKEN_COOKIE, refreshToken.getToken())
                .path("/")
                .httpOnly(true)
                .build();
    }

    private RefreshTokenEntity saveRefreshToken(String email) {
        Optional<RefreshTokenEntity> refreshToken = refreshTokenRepository.findByUser_Email(email);
        if(refreshToken.isPresent()){
            RefreshTokenEntity refreshTokenExist = refreshToken.get();
            refreshTokenExist.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            return refreshTokenRepository.save(refreshTokenExist);
        }else{
            RefreshTokenEntity newRefreshToken = new RefreshTokenEntity();
            newRefreshToken.setUser(userRepository.findByEmail(email).get());
            newRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            newRefreshToken.setToken(UUID.randomUUID().toString());
            RefreshTokenEntity savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
            return savedRefreshToken;
        }
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }
        return token;
    }

    @Override
    public ResponseCookie clearRefreshTokenCookie() {
        ResponseCookie cookie = ResponseCookie.from(AppConstants.NAME_REFRESH_TOKEN_COOKIE, null).path("/").build();
        return cookie;
    }

    @Transactional
    @Override
    public void deleteByUserId(Long userId) {
        try {
            refreshTokenRepository.deleteByUser_Id(userId);
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
