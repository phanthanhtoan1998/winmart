package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.userservice.service.PromotionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController extends BaseController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        super(promotionService);
        this.promotionService = promotionService;
    }
}
