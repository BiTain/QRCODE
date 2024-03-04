package com.bitain.qrcode.config;

import com.bitain.qrcode.dto.user.UserRequest;
import com.bitain.qrcode.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<UserRequest, UserEntity> userTypeMap = modelMapper.createTypeMap(UserRequest.class,UserEntity.class);
        userTypeMap.addMappings(mapper->{
            mapper.skip(UserEntity::setPassWord);
        });
        return modelMapper;
    }
}
