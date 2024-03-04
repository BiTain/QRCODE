package com.bitain.qrcode.repository;

import com.bitain.qrcode.entity.AttendanceEntity;
import com.bitain.qrcode.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends AbstractRepository<AttendanceEntity>{

    AttendanceEntity findByDateAndUser(Date date, UserEntity user);
    List<AttendanceEntity> findAllByDateAndUser(Date date, UserEntity user);
}
