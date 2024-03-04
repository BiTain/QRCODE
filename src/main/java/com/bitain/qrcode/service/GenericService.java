package com.bitain.qrcode.service;

import com.bitain.qrcode.dto.BaseDto;
import com.bitain.qrcode.dto.BaseResponse;
import com.bitain.qrcode.dto.pagination.Pagination;
import com.bitain.qrcode.entity.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GenericService<TDto extends BaseDto, TEntity extends BaseEntity> {
    BaseResponse importDataFromExcelFile(MultipartFile multipartFile);
    Pagination<TDto> findAll(Integer pageNumber, Integer limit);
    TDto findById(Long id);
    BaseResponse saveOrUpdate(TDto dto);
    BaseResponse deleteById(Long id);
    TEntity transformDtoToEntity(TDto dto);
}
