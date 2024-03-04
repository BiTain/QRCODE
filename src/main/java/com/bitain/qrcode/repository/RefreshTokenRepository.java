package com.bitain.qrcode.repository;

import com.bitain.qrcode.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends AbstractRepository<RefreshTokenEntity> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByUser_Email(String email);
    void deleteByUser_Id(Long userId);
}
