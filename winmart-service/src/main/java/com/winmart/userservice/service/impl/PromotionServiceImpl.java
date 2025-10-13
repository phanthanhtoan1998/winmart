package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.response.PromotionResponse;
import com.winmart.userservice.entity.PromotionEntity;
import com.winmart.userservice.repository.PromotionRepository;
import com.winmart.userservice.service.PromotionService;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl extends BaseServiceImpl<PromotionEntity, PromotionResponse> implements PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        super(promotionRepository, PromotionEntity.class, PromotionResponse.class);
        this.promotionRepository = promotionRepository;
    }
}
