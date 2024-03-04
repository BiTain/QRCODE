package com.bitain.qrcode.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bitain.qrcode.dto.BaseDto;
import com.bitain.qrcode.dto.BaseResponse;
import com.bitain.qrcode.dto.pagination.Pagination;
import com.bitain.qrcode.entity.BaseEntity;
import com.bitain.qrcode.exception.ComponentException;
import com.bitain.qrcode.exception.ResourceNotFoundException;
import com.bitain.qrcode.repository.AbstractRepository;
import com.bitain.qrcode.service.GenericService;
import org.springframework.web.multipart.MultipartFile;

@Service
public abstract class AbstractService<TDto extends BaseDto,TEntity extends BaseEntity> implements GenericService<TDto,TEntity> {
    private AbstractRepository<TEntity> repository;
    private Class<TDto> dtoClass;
    private Class<TEntity> entityClass;

    @Autowired
    private ModelMapper modelMapper;
    public AbstractService(AbstractRepository<TEntity> repository,
                           Class<TDto> dtoClass,
                           Class<TEntity> entityClass){
        this.repository = repository;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    @Override
    public Pagination<TDto> findAll(Integer pageNumber, Integer limit) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(pageNumber-1,limit,sort);
		Page<TEntity> page = repository.findAll(pageable);
		List<TEntity> list = page.getContent();
        Pagination<TDto> pg = Pagination.<TDto>builder()
				.listResult(list.stream().map(item->modelMapper.map(item,dtoClass)).collect(Collectors.toList()))
				.totalPage(page.getTotalPages())
				.totalItem((int)page.getTotalElements())
				.pageNumber(page.getNumber())
				.limit(page.getSize())
				.build();
		return pg;
    }

    @Override
    public TDto findById(Long id) {
        TEntity entity = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(entityClass.getName(),"id",String.valueOf(id)));
        return modelMapper.map(entity,dtoClass);
    }

    @Override
    public BaseResponse importDataFromExcelFile(MultipartFile multipartFile) {

        return null;
    }

    @Override
    public BaseResponse saveOrUpdate(TDto dto) {
        try {
            TEntity entity = this.transformDtoToEntity(dto);
            String operationText = entity.getId() != null ? "Update" : "Create";
            this.repository.save(entity);
            return new BaseResponse(200, String.format("%s %s success", operationText, entityClass.getSimpleName().toLowerCase()));
        }catch (Exception e){
            throw new ComponentException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse deleteById(Long id) {
        try {
            TEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName(), "id", String.valueOf(id)));
            repository.delete(entity);
            return new BaseResponse(200, "Delete " + entityClass.getSimpleName().toLowerCase() + " success");
        }catch (Exception e){
            throw new ComponentException(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public TEntity transformDtoToEntity(TDto dto) {
        return modelMapper.map(dto,entityClass);
    }
}
