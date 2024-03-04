package com.bitain.qrcode.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseEntity{
    private String email;
    private String passWord;
    private String fullName;
    private String phone;
    private String address;
    private Boolean enabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @OneToMany(mappedBy = "user")
    private Collection<AttendanceEntity> attendanceEntities;
}
