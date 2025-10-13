package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.response.PaymentResponse;
import com.winmart.userservice.entity.PaymentEntity;
import com.winmart.userservice.repository.PaymentRepository;
import com.winmart.userservice.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends BaseServiceImpl<PaymentEntity, PaymentResponse> implements PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        super(paymentRepository, PaymentEntity.class, PaymentResponse.class);
        this.paymentRepository = paymentRepository;
    }
}
