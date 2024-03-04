package com.bitain.qrcode.service.imp;

import com.bitain.qrcode.dto.auth.AuthRespone;
import com.bitain.qrcode.dto.auth.TokenRefreshResponse;
import com.bitain.qrcode.entity.RefreshTokenEntity;
import com.bitain.qrcode.exception.DisableAccountException;
import com.bitain.qrcode.exception.TokenRefreshException;
import com.bitain.qrcode.repository.RefreshTokenRepository;
import com.bitain.qrcode.service.IAuthService;
import com.bitain.qrcode.service.IRefreshTokenService;
import com.bitain.qrcode.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService implements IAuthService {
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailServiceImpl userDetailService;
    private PasswordEncoder passwordEncoder;
    private IRefreshTokenService refreshTokenService;
    private RefreshTokenRepository refreshTokenRepository;
    @Override
    public AuthRespone authenticateAndGenerateToken(String userName, String passWord) {
        UserDetails userDetails = userDetailService.loadUserByUsername(userName);
        if (!passwordEncoder.matches(passWord,userDetails.getPassword())){
            throw  new BadCredentialsException("Login information is incorrect!");
        }
        if(!userDetails.isEnabled()) {
            throw new DisableAccountException("Account disable!");
        }
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        ResponseCookie resfreshToken = refreshTokenService.generateRefreshTokenCookie(userDetails.getUsername());
        return AuthRespone.builder()
                .accessToken(accessToken)
                .refreshToken(resfreshToken)
                .build();
    }

    @Override
    public TokenRefreshResponse processRefreshToken(HttpServletRequest request) {
        String requestRefreshToken = refreshTokenService.getRefreshTokenFromCookies(request);
        if(requestRefreshToken == null){
            throw new TokenRefreshException("Refresh token not found. Please make a new login request!");
        }
        TokenRefreshResponse tokenRefreshResponse = refreshTokenRepository.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(userEntity->{
                    String token = jwtTokenUtil.generateToken(userDetailService.loadUserByUsername(userEntity.getEmail()));
                    return TokenRefreshResponse.builder()
                            .newAccessToken(token)
                            .build();
                })
                .orElseThrow(()->new TokenRefreshException(requestRefreshToken,"Refresh token is not in database!"));
        return tokenRefreshResponse;
    }

    @Override
    public ResponseCookie doLogout(Long userId) {
        refreshTokenService.deleteByUserId(userId);
        return refreshTokenService.clearRefreshTokenCookie();
    }
}
