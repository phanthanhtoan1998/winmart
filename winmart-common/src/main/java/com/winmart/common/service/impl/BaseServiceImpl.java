package com.winmart.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winmart.common.Constant.MessageConstants;
import com.winmart.common.dto.BaseDto;
import com.winmart.common.dto.RequestDto;
import com.winmart.common.exception.BusinessException;
import com.winmart.common.model.BaseEntity;
import com.winmart.common.repository.BaseRepository;
import com.winmart.common.service.BaseService;
import com.winmart.common.specification.BaseSpecification;
import com.winmart.common.util.DataUtil;
import com.winmart.common.util.MessageCommon;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

public class BaseServiceImpl<T extends BaseEntity, DTO extends BaseDto>
        implements BaseService<T, DTO> {

    BaseRepository jpaRepository;
    Class<DTO> dtoClass;
    Class<T> entityClass;
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private AuditReader auditReader;

    @Autowired
    public MessageCommon messageCommon;

    protected Logger LOG = LogManager.getLogger(this.getClass());

    public BaseServiceImpl(BaseRepository jpaRepository, Class<T> entityClass, Class<DTO> dtoClass) {
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DTO> findByIds(List<Long> ids) {
        List<T> data = jpaRepository.findAllById(ids);
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Transactional
    @Override
    public DTO saveObject(DTO data) {
        T model = DataUtil.convertObject(data, x -> modelMapper.map(x, entityClass));
        return DataUtil.convertObject(jpaRepository.save(model), x -> modelMapper.map(x, dtoClass));
    }

  @SuppressWarnings("unchecked")
  @Transactional
  @Override
  public DTO saveFromObject(Object data) {
    try {
      // Convert Object (LinkedHashMap) to DTO using ObjectMapper
      DTO dto;
      if (data instanceof java.util.Map) {
        // If data is Map (from JSON), convert to DTO
        dto = objectMapper.convertValue(data, dtoClass);
      } else if (data instanceof BaseDto) {
        dto = (DTO) data;
      } else {
        dto = objectMapper.convertValue(data, dtoClass);
      }
      
      T model = DataUtil.convertObject(dto, x -> modelMapper.map(x, entityClass));
      return DataUtil.convertObject(jpaRepository.save(model), x -> modelMapper.map(x, dtoClass));
    } catch (Exception e) {
      LOG.error("Error saving object: ", e);
      throw new BusinessException("Error saving object: " + e.getMessage());
    }
  }

    @Transactional
    @Override
    public void saveListObject(List<DTO> data) {
        List<T> model = DataUtil.convertList(data, x -> modelMapper.map(x, entityClass));
        jpaRepository.saveAll(model);
    }

  @SuppressWarnings("unchecked")
  @Transactional
  @Override
  public void saveListFromObject(List<Object> data) {
    try {
      // Convert List of Objects (LinkedHashMap) to List of DTOs
      List<DTO> dtoList = DataUtil.convertList(data, item -> {
        if (item instanceof java.util.Map) {
          return objectMapper.convertValue(item, dtoClass);
        } else if (item instanceof BaseDto) {
          return (DTO) item;
        } else {
          return objectMapper.convertValue(item, dtoClass);
        }
      });
      
      List<T> model = DataUtil.convertList(dtoList, x -> modelMapper.map(x, entityClass));
      jpaRepository.saveAll(model);
    } catch (Exception e) {
      LOG.error("Error saving list of objects: ", e);
      throw new BusinessException("Error saving list: " + e.getMessage());
    }
  }

    @Override
    public List<DTO> findAllNonePage(RequestDto requestDto) {
        if (jpaRepository == null) {
            throw new BusinessException(MessageConstants.NULL_POINT_EXCEPTION);
        }
        BaseSpecification<T> baseSpecification = new BaseSpecification();
        baseSpecification.add(requestDto.getLsCondition());
        Sort sort = DataUtil.getSort(requestDto);
        return this.findAll(baseSpecification, sort);
    }

    @Override
    public List<DTO> findAll() {
        List<T> data = jpaRepository.findAll();
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        List<T> data = jpaRepository.findAllById(ids);
        data.forEach(
                d -> {
                    d.setIsDeleted(true);
                });
        jpaRepository.saveAll(data);
    }

    @Override
    public void deleteData(List<T> ids) {
        jpaRepository.deleteAll(ids);
    }

    @Override
    public List<DTO> findAll(@Nullable Specification<T> spec) {
        List<T> data = jpaRepository.findAll(spec);
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Override
    public Page<DTO> findAll(@Nullable Specification<T> spec, Pageable pageable) {
        Page<T> data = jpaRepository.findAll(spec, pageable);
        return data.map(x -> modelMapper.map(x, dtoClass));
    }

    @Override
    public Page<DTO> findAll(RequestDto requestDto) {
        BaseSpecification<T> baseSpecification = new BaseSpecification();
        baseSpecification.add(requestDto.getLsCondition());
        Pageable pageable = DataUtil.getPageable(requestDto);
        return this.findAll(baseSpecification, pageable);
    }

    @Override
    public List<DTO> findAll(@Nullable Specification<T> spec, Sort sort) {
        List<T> data = jpaRepository.findAll(spec, sort);
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Override
    public Long count(@Nullable Specification<T> spec) {
        return jpaRepository.count(spec);
    }

    @Override
    public List<DTO> getRevisionById(List<Long> ids) {
        AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(entityClass, true);

        auditQuery.add(AuditEntity.id().in(ids));
        return DataUtil.convertList(auditQuery.getResultList(), x -> modelMapper.map(x, dtoClass));
    }
}
