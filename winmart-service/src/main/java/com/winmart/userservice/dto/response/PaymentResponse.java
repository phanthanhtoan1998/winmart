package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse extends BaseDto {
    private Long paymentId;
    private Long invoiceId;
    private BigDecimal amount;
    private String paymentMethod;
    private String qrCodeUrl;
    private String paymentStatus;
    private LocalDateTime createdAt;
}
