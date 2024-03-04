package com.bitain.qrcode.service.imp;

import com.bitain.qrcode.dto.RoleDto;
import com.bitain.qrcode.entity.RoleEntity;
import com.bitain.qrcode.repository.AbstractRepository;
import com.bitain.qrcode.service.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<RoleDto, RoleEntity> implements IRoleService {
    public RoleService(AbstractRepository<RoleEntity> repository) {
        super(repository, RoleDto.class, RoleEntity.class);
    }
}

