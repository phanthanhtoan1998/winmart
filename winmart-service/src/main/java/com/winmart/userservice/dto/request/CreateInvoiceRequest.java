package com.winmart.userservice.dto.request;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInvoiceRequest extends BaseDto {
    private Long userId;
    private String customerName;
    private String customerPhone; // Thêm số điện thoại khách hàng
    private BigDecimal totalAmount;
    private String status;
    private List<InvoiceItemRequest> items;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class InvoiceItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
