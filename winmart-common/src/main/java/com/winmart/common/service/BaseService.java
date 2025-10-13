package com.winmart.common.service;

import com.winmart.common.dto.BaseDto;
import com.winmart.common.dto.RequestDto;
import com.winmart.common.model.BaseEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public interface BaseService<T extends BaseEntity, DTO extends BaseDto> {
    List<DTO> findByIds(List<Long> ids);

    List<DTO> getRevisionById(List<Long> ids);

    List<DTO> findAll();

    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    void deleteData(List<T> ids);

    List<DTO> findAll(@Nullable Specification<T> spec);

    Page<DTO> findAll(@Nullable Specification<T> spec, Pageable pageable);

    Page<DTO> findAll(RequestDto requestDto);

    List<DTO> findAll(@Nullable Specification<T> spec, Sort sort);

    Long count(@Nullable Specification<T> spec);

  DTO saveObject(DTO data);

  void saveListObject(List<DTO> data);

  List<DTO> findAllNonePage(RequestDto requestDto);

  // Methods to accept Object (for LinkedHashMap from JSON)
  DTO saveFromObject(Object data);

  void saveListFromObject(List<Object> data);
}
