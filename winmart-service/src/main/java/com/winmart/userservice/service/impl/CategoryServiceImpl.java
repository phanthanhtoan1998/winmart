package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.common.util.DataUtil;
import com.winmart.userservice.dto.response.CategoryResponse;
import com.winmart.userservice.entity.CategoryEntity;
import com.winmart.userservice.repository.CategoryRepository;
import com.winmart.userservice.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity, CategoryResponse> implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository, CategoryEntity.class, CategoryResponse.class);
        this.categoryRepository = categoryRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CategoryResponse saveFromObject(Object data) {
        try {
            // Convert Object to DTO
            CategoryResponse dto;
            if (data instanceof java.util.Map) {
                dto = objectMapper.convertValue(data, CategoryResponse.class);
            } else if (data instanceof CategoryResponse) {
                dto = (CategoryResponse) data;
            } else {
                dto = objectMapper.convertValue(data, CategoryResponse.class);
            }
            
            // Convert DTO to Entity
            CategoryEntity entity = modelMapper.map(dto, CategoryEntity.class);
            
            // Handle parent relationship
            if (dto.getParentId() != null) {
                CategoryEntity parent = categoryRepository.findById(dto.getParentId())
                        .orElseThrow(() -> new RuntimeException("Parent category not found with ID: " + dto.getParentId()));
                entity.setParent(parent);
            }
            
            // Save
            CategoryEntity savedEntity = categoryRepository.save(entity);
            
            // Convert back to response
            CategoryResponse response = modelMapper.map(savedEntity, CategoryResponse.class);
            response.setCategoryId(savedEntity.getId());
            if (savedEntity.getParent() != null) {
                response.setParentId(savedEntity.getParent().getId());
                response.setParentName(savedEntity.getParent().getName());
            }
            
            return response;
        } catch (Exception e) {
            LOG.error("Error saving category from object: ", e);
            throw new RuntimeException("Error saving category: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveListFromObject(java.util.List<Object> dataList) {
        try {
            for (Object data : dataList) {
                // Convert Object to DTO
                CategoryResponse dto;
                if (data instanceof java.util.Map) {
                    dto = objectMapper.convertValue(data, CategoryResponse.class);
                } else if (data instanceof CategoryResponse) {
                    dto = (CategoryResponse) data;
                } else {
                    dto = objectMapper.convertValue(data, CategoryResponse.class);
                }
                
                // Convert DTO to Entity
                CategoryEntity entity = modelMapper.map(dto, CategoryEntity.class);
                
                // Handle parent relationship
                if (dto.getParentId() != null) {
                    CategoryEntity parent = categoryRepository.findById(dto.getParentId())
                            .orElseThrow(() -> new RuntimeException("Parent category not found with ID: " + dto.getParentId()));
                    entity.setParent(parent);
                }
                
                // Save
                categoryRepository.save(entity);
            }
        } catch (Exception e) {
            LOG.error("Error saving list of categories from object: ", e);
            throw new RuntimeException("Error saving list of categories: " + e.getMessage());
        }
    }

    @Override
    public List<CategoryResponse> getRootCategories() {
        List<CategoryEntity> rootCategories = categoryRepository.findByParentIsNull();
        return DataUtil.convertList(rootCategories, entity -> {
            CategoryResponse response = modelMapper.map(entity, CategoryResponse.class);
            response.setCategoryId(entity.getId());
            return response;
        });
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        List<CategoryEntity> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(this::buildCategoryTree)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getChildren(Long parentId) {
        List<CategoryEntity> children = categoryRepository.findByParentId(parentId);
        return DataUtil.convertList(children, entity -> {
            CategoryResponse response = modelMapper.map(entity, CategoryResponse.class);
            response.setCategoryId(entity.getId());
            if (entity.getParent() != null) {
                response.setParentId(entity.getParent().getId());
                response.setParentName(entity.getParent().getName());
            }
            return response;
        });
    }

    /**
     * Recursively build category tree
     */
    private CategoryResponse buildCategoryTree(CategoryEntity entity) {
        CategoryResponse response = modelMapper.map(entity, CategoryResponse.class);
        response.setCategoryId(entity.getId());
        
        if (entity.getParent() != null) {
            response.setParentId(entity.getParent().getId());
            response.setParentName(entity.getParent().getName());
        }
        
        // Recursively build children
        List<CategoryEntity> children = categoryRepository.findByParentId(entity.getId());
        if (children != null && !children.isEmpty()) {
            response.setChildren(children.stream()
                    .map(this::buildCategoryTree)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }
}
