package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.request.CreateInvoiceRequest;
import com.winmart.userservice.dto.response.InvoiceResponse;
import com.winmart.userservice.dto.response.UserOrderCountResponse;
import com.winmart.userservice.entity.InvoiceEntity;
import com.winmart.userservice.repository.InvoiceRepository;
import com.winmart.userservice.service.InvoiceService;
import com.winmart.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
public class InvoiceServiceImpl extends BaseServiceImpl<InvoiceEntity, InvoiceResponse> implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserService userService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, UserService userService) {
        super(invoiceRepository, InvoiceEntity.class, InvoiceResponse.class);
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }

    @Override
    public InvoiceResponse saveFromObject(Object data) {
        try {
            // Convert Object to DTO
            CreateInvoiceRequest dto;
            if (data instanceof java.util.Map) {
                dto = objectMapper.convertValue(data, CreateInvoiceRequest.class);
            } else if (data instanceof CreateInvoiceRequest) {
                dto = (CreateInvoiceRequest) data;
            } else {
                dto = objectMapper.convertValue(data, CreateInvoiceRequest.class);
            }

            // Convert to Entity
            InvoiceEntity entity = modelMapper.map(dto, InvoiceEntity.class);

            // Save invoice
            InvoiceEntity savedEntity = invoiceRepository.save(entity);

            // Tích điểm nếu có số điện thoại khách hàng
            if (dto.getCustomerPhone() != null && !dto.getCustomerPhone().trim().isEmpty()) {
                try {
                    // Tính điểm dựa trên tổng tiền (ví dụ: 1 điểm cho mỗi 10,000 VND)
                    BigDecimal pointsToAdd = dto.getTotalAmount().divide(new BigDecimal("10000"), 0, RoundingMode.DOWN);
                    Integer points = pointsToAdd.intValue();

                    if (points > 0) {
                        ResponseEntity<String> pointsResponse = userService.addPointsByPhoneOrCreateUser(
                                dto.getCustomerPhone(), dto.getCustomerName(), points);
                        if (pointsResponse.getStatusCode().is2xxSuccessful()) {
                            log.info("Points added successfully for customer phone {}: {} points",
                                    dto.getCustomerPhone(), points);
                        } else {
                            log.warn("Failed to add points for customer phone {}: {}",
                                    dto.getCustomerPhone(), pointsResponse.getBody());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error adding points for customer phone {}: {}", dto.getCustomerPhone(), e.getMessage());
                    // Không throw exception để không ảnh hưởng đến việc tạo invoice
                }
            } else {
                log.info("No customer phone provided, skipping points addition for invoice {}", savedEntity.getId());
            }

            return modelMapper.map(savedEntity, InvoiceResponse.class);
        } catch (Exception e) {
            log.error("Error saving invoice from object: ", e);
            throw new RuntimeException("Error saving invoice: " + e.getMessage());
        }
    }

    @Override
    public UserOrderCountResponse getUserOrderCount(Long userId) {
        try {
            // Get user info

            // Count orders by status
            Long totalOrders = invoiceRepository.countByUserId(userId);
            Long pendingOrders = invoiceRepository.countByUserIdAndStatus(userId, InvoiceEntity.InvoiceStatus.PENDING);
            Long paidOrders = invoiceRepository.countByUserIdAndStatus(userId, InvoiceEntity.InvoiceStatus.PAID);
            Long cancelledOrders = invoiceRepository.countByUserIdAndStatus(userId, InvoiceEntity.InvoiceStatus.CANCELLED);

            return UserOrderCountResponse.builder()
                    .userId(userId)
                    .totalOrders(totalOrders)
                    .pendingOrders(pendingOrders)
                    .paidOrders(paidOrders)
                    .cancelledOrders(cancelledOrders)
                    .build();
        } catch (Exception e) {
            log.error("Error getting user order count for userId {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error getting user order count: " + e.getMessage());
        }
    }

    @Override
    public List<UserOrderCountResponse> getUserOrderCounts(List<Long> userIds) {
        return userIds.stream()
                .map(this::getUserOrderCount)
                .toList();
    }

}
