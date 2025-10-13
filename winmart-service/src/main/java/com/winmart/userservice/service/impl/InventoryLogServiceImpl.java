package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.response.InventoryLogResponse;
import com.winmart.userservice.entity.InventoryLogEntity;
import com.winmart.userservice.repository.InventoryLogRepository;
import com.winmart.userservice.service.InventoryLogService;
import org.springframework.stereotype.Service;

@Service
public class InventoryLogServiceImpl extends BaseServiceImpl<InventoryLogEntity, InventoryLogResponse> implements InventoryLogService {
    private final InventoryLogRepository inventoryLogRepository;

    public InventoryLogServiceImpl(InventoryLogRepository inventoryLogRepository) {
        super(inventoryLogRepository, InventoryLogEntity.class, InventoryLogResponse.class);
        this.inventoryLogRepository = inventoryLogRepository;
    }
}