package com.bitain.qrcode.repository;

import com.bitain.qrcode.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractRepository<TEntity extends BaseEntity> extends JpaRepository<TEntity,Long> {
}
