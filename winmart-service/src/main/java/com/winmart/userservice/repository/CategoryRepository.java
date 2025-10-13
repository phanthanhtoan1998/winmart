package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity, Long> {
    
    /**
     * Find all root categories (parent is null)
     */
    List<CategoryEntity> findByParentIsNull();
    
    /**
     * Find all children of a parent category
     */
    List<CategoryEntity> findByParentId(Long parentId);
}
