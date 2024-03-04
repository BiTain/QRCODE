package com.bitain.qrcode.controller;

import com.bitain.qrcode.dto.BaseResponse;
import com.bitain.qrcode.dto.Qrcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bitain.qrcode.dto.user.UserDto;
import com.bitain.qrcode.dto.user.UserRequest;
import com.bitain.qrcode.entity.UserEntity;
import com.bitain.qrcode.service.IUserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserAPI extends BaseAPI<UserEntity,UserDto,UserRequest>{
    private IUserService userService;
    public UserAPI(IUserService userService){
        super(userService);
        this.userService = userService;
    }

    @PostMapping("/attendance/{id}")
    public ResponseEntity<BaseResponse> attendance(@RequestBody Qrcode qrcode, @PathVariable("id") Long id){
        return ResponseEntity.ok(userService.attendance(qrcode.getQrcode(), id));
    }
}
