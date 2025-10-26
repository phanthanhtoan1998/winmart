package com.winmart.userservice.service;

import com.winmart.common.service.BaseService;
import com.winmart.userservice.dto.request.CreatePaymentRequest;
import com.winmart.userservice.dto.response.PaymentResponse;
import com.winmart.userservice.entity.PaymentEntity;

import java.util.Map;

public interface PaymentService extends BaseService<PaymentEntity, PaymentResponse> {

    PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse updatePaymentStatus(Long paymentId, String status);

    PaymentResponse cancelPayment(Long paymentId);

    PaymentResponse callback(Map<String, String> payload);
}
