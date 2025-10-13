package com.winmart.userservice.dto.request;

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
public class CreatePromotionRequest extends BaseDto {
    private String code;
    private BigDecimal discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
