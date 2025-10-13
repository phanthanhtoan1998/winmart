package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.userservice.service.InvoiceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController extends BaseController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        super(invoiceService);
        this.invoiceService = invoiceService;
    }
}
