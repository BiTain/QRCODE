package com.bitain.qrcode.service;

import java.util.List;

import com.bitain.qrcode.dto.BaseResponse;
import com.bitain.qrcode.dto.user.UserDto;
import com.bitain.qrcode.entity.UserEntity;

public interface IUserService extends GenericService<UserDto,UserEntity>{
    void deleteUsers(List<Long> idusers);
    BaseResponse attendance(String qrCode, Long idUser);
}
