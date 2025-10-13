package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.userservice.service.InventoryLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory-logs")
public class InventoryLogController extends BaseController {

    private final InventoryLogService inventoryLogService;

    public InventoryLogController(InventoryLogService inventoryLogService) {
        super(inventoryLogService);
        this.inventoryLogService = inventoryLogService;
    }
}
