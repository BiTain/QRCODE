package com.bitain.qrcode.repository;

import com.bitain.qrcode.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends AbstractRepository<UserEntity> {
    Optional<UserEntity> findByEmail(String email);
}
