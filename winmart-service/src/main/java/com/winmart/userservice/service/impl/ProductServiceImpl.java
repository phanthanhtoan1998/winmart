package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.response.ProductResponse;
import com.winmart.userservice.entity.ProductEntity;
import com.winmart.userservice.repository.ProductRepository;
import com.winmart.userservice.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductEntity, ProductResponse> implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository, ProductEntity.class, ProductResponse.class);
        this.productRepository = productRepository;
    }
}
