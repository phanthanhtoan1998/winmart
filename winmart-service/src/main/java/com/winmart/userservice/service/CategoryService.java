package com.winmart.userservice.service;

import com.winmart.common.service.BaseService;
import com.winmart.userservice.dto.response.CategoryResponse;
import com.winmart.userservice.entity.CategoryEntity;

import java.util.List;

public interface CategoryService extends BaseService<CategoryEntity, CategoryResponse> {
    
    /**
     * Get all root categories (categories without parent)
     */
    List<CategoryResponse> getRootCategories();
    
    /**
     * Get full category tree (hierarchical structure)
     */
    List<CategoryResponse> getCategoryTree();
    
    /**
     * Get children of a specific category
     */
    List<CategoryResponse> getChildren(Long parentId);
}
