package com.bitain.qrcode.dto.user;

import com.bitain.qrcode.dto.BaseDto;
import com.bitain.qrcode.dto.RoleDto;
import lombok.*;

import java.util.Set;

@Setter
@Getter
public class UserDto extends BaseDto {
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Set<RoleDto> roleDtos;
}
