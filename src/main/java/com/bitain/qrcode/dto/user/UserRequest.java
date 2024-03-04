package com.bitain.qrcode.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequest extends UserDto{
    private String passWord;
    private MultipartFile imageFile;
}
