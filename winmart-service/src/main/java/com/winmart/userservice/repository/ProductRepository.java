package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity, Long> {}
