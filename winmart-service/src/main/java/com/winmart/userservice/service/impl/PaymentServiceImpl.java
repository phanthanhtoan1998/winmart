package com.winmart.userservice.service.impl;

import com.winmart.common.exception.ResponseConfig;
import com.winmart.common.mapper.Mapper;
import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.request.CreatePaymentRequest;
import com.winmart.userservice.dto.response.PaymentResponse;
import com.winmart.userservice.entity.InvoiceEntity;
import com.winmart.userservice.entity.PaymentEntity;
import com.winmart.userservice.repository.InvoiceRepository;
import com.winmart.userservice.repository.PaymentRepository;
import com.winmart.userservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PaymentServiceImpl extends BaseServiceImpl<PaymentEntity, PaymentResponse> implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository, ModelMapper modelMapper) {
        super(paymentRepository, PaymentEntity.class, PaymentResponse.class);
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest) {
        LOG.info("Creating payment for invoice: {}", createPaymentRequest.getInvoiceId());

        InvoiceEntity invoiceEntity = invoiceRepository.findById(createPaymentRequest.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Tạo PaymentEntity
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .invoice(invoiceEntity)
                .amount(createPaymentRequest.getAmount())
                .paymentMethod(createPaymentRequest.getPaymentMethod())
                .paymentStatus(PaymentEntity.PaymentStatus.UNPAID)
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu vào database trước để có ID
        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);

        // Tạo QR code URL sau khi đã có ID
        String qrCodeUrl = generateQrCodeUrl(createPaymentRequest.getAmount(), savedPayment.getId());
        savedPayment.setQrCodeUrl(qrCodeUrl);

        // Cập nhật lại với QR code URL
        savedPayment = paymentRepository.save(savedPayment);

        // Convert to response
        return modelMapper.map(savedPayment, PaymentResponse.class);
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        LOG.info("Getting payment by id: {}", paymentId);
        Optional<PaymentEntity> paymentEntity = paymentRepository.findById(paymentId);

        if (paymentEntity.isEmpty()) {
            throw new RuntimeException("Payment not found with id: " + paymentId);
        }

        return modelMapper.map(paymentEntity.get(), PaymentResponse.class);
    }

    @Override
    public PaymentResponse updatePaymentStatus(Long paymentId, String status) {
        LOG.info("Updating payment status for paymentId: {} to {}", paymentId, status);

        Optional<PaymentEntity> paymentEntityOpt = paymentRepository.findById(paymentId);
        if (paymentEntityOpt.isEmpty()) {
            throw new RuntimeException("Payment not found with id: " + paymentId);
        }

        PaymentEntity paymentEntity = paymentEntityOpt.get();
        paymentEntity.setPaymentStatus(PaymentEntity.PaymentStatus.valueOf(status.toUpperCase()));

        PaymentEntity updatedPayment = paymentRepository.save(paymentEntity);
        return modelMapper.map(updatedPayment, PaymentResponse.class);
    }

    @Override
    public PaymentResponse cancelPayment(Long paymentId) {
        LOG.info("Cancelling payment for paymentId: {}", paymentId);

        Optional<PaymentEntity> paymentEntityOpt = paymentRepository.findById(paymentId);
        if (paymentEntityOpt.isEmpty()) {
            throw new RuntimeException("Payment not found with id: " + paymentId);
        }

        PaymentEntity paymentEntity = paymentEntityOpt.get();
        paymentEntity.setPaymentStatus(PaymentEntity.PaymentStatus.FAILED);

        PaymentEntity cancelledPayment = paymentRepository.save(paymentEntity);
        return modelMapper.map(cancelledPayment, PaymentResponse.class);
    }

    @Override
    public PaymentResponse callback(Map<String, String> payload) {
        String paymentId = payload.get("content");
        String transferAmount = payload.get("transferAmount");

        Pattern pattern = Pattern.compile("PAY[^.]+");
        Matcher matcher = pattern.matcher(paymentId);
        if (!matcher.find()) {
            throw new RuntimeException("Invalid webhook!");
        } else {
            paymentId = matcher.group();
            Optional<PaymentEntity> paymentEntityOpt = paymentRepository.findById(Long.valueOf(paymentId));
            if (paymentEntityOpt.isEmpty()) {
                throw new RuntimeException("Payment not found with id: " + paymentId);
            }
            if (!BigDecimal.valueOf(Long.parseLong(transferAmount)).equals(paymentEntityOpt.get().getAmount())) {
                throw new RuntimeException("Payment with id: " + paymentId + " does not match transferAmount");
            }
            PaymentResponse paymentResponse = updatePaymentStatus(Long.valueOf(paymentId), "PAID");
            Optional<InvoiceEntity> byId = invoiceRepository.findById(paymentResponse.getInvoiceId());
            if (byId.isPresent()) {
                InvoiceEntity invoiceEntity = byId.get();
                invoiceEntity.setStatus(InvoiceEntity.InvoiceStatus.PAID);
                invoiceRepository.save(invoiceEntity);
            }
            return paymentResponse;
        }
    }

    private String generateQrCodeUrl(java.math.BigDecimal amount, Long paymentId) {
        // Tạo URL QR code cho SePay
        return String.format("https://qr.sepay.vn/img?bank=TPBank&acc=32271002004&template=compact&amount=%s&des=%s",
                amount, "PAY" + paymentId);
    }
}
