package com.bitain.qrcode.controller;

import com.bitain.qrcode.dto.auth.AuthRequest;
import com.bitain.qrcode.dto.auth.AuthRespone;
import com.bitain.qrcode.dto.auth.TokenRefreshResponse;
import com.bitain.qrcode.service.imp.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/authenticate")
public class AuthAPI {
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthRespone> authenticateUser(@RequestBody AuthRequest authRequest) {
        AuthRespone loginResponse = authService.authenticateAndGenerateToken(authRequest.getEmail(),authRequest.getPassWord());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResponse.getRefreshToken().toString())
                .body(loginResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(HttpServletRequest request) {
        TokenRefreshResponse res = authService.processRefreshToken(request);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable("id") Long userId) {
        ResponseCookie clearCookie = authService.doLogout(userId);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .build();
    }
}
