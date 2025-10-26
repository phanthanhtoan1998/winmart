package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.request.CreatePaymentRequest;
import com.winmart.userservice.dto.response.PaymentResponse;
import com.winmart.userservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController extends BaseController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        super(paymentService);
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<ResponseDto<PaymentResponse>> generateQrCode(@RequestBody CreatePaymentRequest createPaymentRequest) {
        LOG.info("Generating QR code for payment: {}", createPaymentRequest);
        PaymentResponse paymentResponse = paymentService.createPayment(createPaymentRequest);
        return ResponseConfig.success(paymentResponse);
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentResponse>> checkPaymentStatus(@PathVariable Long paymentId) {
        LOG.info("Checking payment status for paymentId: {}", paymentId);
        PaymentResponse paymentResponse = paymentService.getPaymentById(paymentId);
        return ResponseConfig.success(paymentResponse);
    }

    @PostMapping("/webhook")
    public ResponseEntity<ResponseDto<PaymentResponse>> paymentWebhook(
            @RequestBody Map<String, String> payload) {
        LOG.info("Received payment webhook: {}", payload);
        PaymentResponse callbackResponse = paymentService.callback(payload);
        return ResponseConfig.success(callbackResponse);
    }

    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentResponse>> cancelPayment(@PathVariable Long paymentId) {
        LOG.info("Cancelling payment for paymentId: {}", paymentId);
        PaymentResponse paymentResponse = paymentService.cancelPayment(paymentId);
        return ResponseConfig.success(paymentResponse);
    }

}
