package com.winmart.userservice.dto.request;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentRequest extends BaseDto {
    private Long invoiceId;
    private BigDecimal amount;
    private String paymentMethod;
    private String qrCodeUrl;
    private String paymentStatus;
}
