package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionResponse extends BaseDto {
    private Long id;
    private String code;
    private BigDecimal discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long productId;
}
