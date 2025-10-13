package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.PromotionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends BaseRepository<PromotionEntity, Long> {}
