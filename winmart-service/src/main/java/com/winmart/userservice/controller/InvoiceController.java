package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.request.UserOrderCountRequest;
import com.winmart.userservice.dto.response.UserOrderCountResponse;
import com.winmart.userservice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController extends BaseController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        super(invoiceService);
        this.invoiceService = invoiceService;
    }

    @GetMapping("/user/{userId}/order-count")
    public ResponseEntity<ResponseDto<UserOrderCountResponse>> getUserOrderCount(@PathVariable Long userId) {
        try {
            UserOrderCountResponse response = invoiceService.getUserOrderCount(userId);
            return ResponseConfig.success(response, "User order count retrieved successfully");
        } catch (Exception e) {
            return ResponseConfig.badRequest("Failed to get user order count: " + e.getMessage());
        }
    }

    @PostMapping("/users/order-counts")
    public ResponseEntity<ResponseDto<List<UserOrderCountResponse>>> getUserOrderCounts(@RequestBody UserOrderCountRequest request) {
        try {
            List<UserOrderCountResponse> responses = invoiceService.getUserOrderCounts(request.getUserIds());
            return ResponseConfig.success(responses, "User order counts retrieved successfully");
        } catch (Exception e) {
            return ResponseConfig.badRequest("Failed to get user order counts: " + e.getMessage());
        }
    }

}
