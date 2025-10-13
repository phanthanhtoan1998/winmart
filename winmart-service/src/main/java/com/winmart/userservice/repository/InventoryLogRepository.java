package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.InventoryLogEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryLogRepository extends BaseRepository<InventoryLogEntity, Long> {}
