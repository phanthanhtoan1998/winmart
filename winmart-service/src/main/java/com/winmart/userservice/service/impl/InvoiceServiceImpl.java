package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.response.InvoiceResponse;
import com.winmart.userservice.entity.InvoiceEntity;
import com.winmart.userservice.repository.InvoiceRepository;
import com.winmart.userservice.service.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl extends BaseServiceImpl<InvoiceEntity, InvoiceResponse> implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        super(invoiceRepository, InvoiceEntity.class, InvoiceResponse.class);
        this.invoiceRepository = invoiceRepository;
    }
}
