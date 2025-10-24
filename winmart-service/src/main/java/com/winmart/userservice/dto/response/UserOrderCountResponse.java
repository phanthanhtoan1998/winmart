package com.winmart.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderCountResponse {
    private Long userId;
    private String userName;
    private Long totalOrders;
    private Long pendingOrders;
    private Long paidOrders;
    private Long cancelledOrders;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserOrderCountListResponse {
    private List<UserOrderCountResponse> userOrderCounts;
    private Integer totalUsers;
}
