package com.winmart.userservice.service;

import com.winmart.common.service.BaseService;
import com.winmart.userservice.dto.response.InvoiceResponse;
import com.winmart.userservice.dto.response.UserOrderCountResponse;
import com.winmart.userservice.entity.InvoiceEntity;

import java.util.List;

public interface InvoiceService extends BaseService<InvoiceEntity, InvoiceResponse> {
    UserOrderCountResponse getUserOrderCount(Long userId);
}
