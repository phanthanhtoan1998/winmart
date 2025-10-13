package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryLogResponse extends BaseDto {
    private Long logId;
    private Long productId;
    private String productName;
    private Long userId;
    private String userName;
    private String actionType;
    private Integer quantity;
    private String note;
    private LocalDateTime createdAt;
}
