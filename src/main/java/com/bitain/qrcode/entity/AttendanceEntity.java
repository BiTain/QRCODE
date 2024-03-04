package com.bitain.qrcode.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "attendance")
@Getter
@Setter
public class AttendanceEntity extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.TIME)
    private LocalTime timeIn;
    @Temporal(TemporalType.TIME)
    private LocalTime timeOut;
    private Boolean status = false;
}
