package com.winmart.userservice.dto.request;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInventoryLogRequest extends BaseDto {
    private Long productId;
    private Long userId;
    private String actionType;
    private Integer quantity;
    private String note;
}
