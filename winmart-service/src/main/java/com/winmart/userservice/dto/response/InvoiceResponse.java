package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse extends BaseDto {
    private Long invoiceId;
    private Long userId;
    private String customerName;
    private String customerPhone;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<InvoiceItemResponse> items;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class InvoiceItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
