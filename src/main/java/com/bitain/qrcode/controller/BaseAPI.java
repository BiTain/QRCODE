package com.bitain.qrcode.controller;

import com.bitain.qrcode.constants.AppConstants;
import com.bitain.qrcode.dto.BaseDto;
import com.bitain.qrcode.dto.pagination.Pagination;
import com.bitain.qrcode.entity.BaseEntity;
import com.bitain.qrcode.service.GenericService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class BaseAPI<TEntity extends BaseEntity, TDto extends BaseDto, TDtoRequest extends TDto> {
    private GenericService<TDto,TEntity> service;
    public BaseAPI(GenericService<TDto,TEntity> service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Pagination<TDto>> findAll(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
                                                    @RequestParam(value = "limit", defaultValue = AppConstants.USER_DEFAULT_PAGE_SIZE) Integer limit){
        Pagination<TDto> listResponse = service.findAll(page,limit);
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TDto> findById(@PathVariable("id") Long id){
        TDto response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TDtoRequest tDtoRequest){
        return ResponseEntity.ok(service.saveOrUpdate(tDtoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TDtoRequest tDtoRequest){
        tDtoRequest.setId(id);
        return ResponseEntity.ok(service.saveOrUpdate(tDtoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.deleteById(id));
    }
}
