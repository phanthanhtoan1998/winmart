package com.winmart.common.controller;

import com.winmart.common.dto.BaseDto;
import com.winmart.common.dto.RequestDto;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.common.service.BaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

public class BaseController {
    public final BaseService baseService;
    protected Logger LOG = LogManager.getLogger(this.getClass());

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    // ========================================
    // CREATE APIs - TẠO MỚI
    // ========================================

    /**
     * TẠO MỚI một record
     * POST /create
     * Body: DTO object (không cần có ID)
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Object>> create(
            HttpServletRequest httpServletRequest, @RequestBody Object dto) {
        LOG.info("Creating new record");
        Object result = baseService.saveFromObject(dto);
        return ResponseConfig.success(result);
    }

    /**
     * TẠO MỚI nhiều records
     * POST /createList
     * Body: List<DTO> (không cần có ID)
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/createList")
    public ResponseEntity<ResponseDto<String>> createList(
            HttpServletRequest httpServletRequest, @RequestBody List<Object> dtos) {
        LOG.info("Creating {} new records", dtos.size());
        baseService.saveListFromObject(dtos);
        return ResponseConfig.success("Created " + dtos.size() + " records successfully");
    }

    // ========================================
    // UPDATE APIs - CẬP NHẬT
    // ========================================

    /**
     * CẬP NHẬT một record
     * PUT /update
     * Body: DTO object (phải có ID)
     */
    @SuppressWarnings("unchecked")
    @PutMapping("/update")
    public ResponseEntity<ResponseDto<Object>> update(
            HttpServletRequest httpServletRequest,
            @RequestBody Object dto) {
        LOG.info("Updating record");
        Object result = baseService.saveFromObject(dto);
        return ResponseConfig.success(result);
    }

    /**
     * CẬP NHẬT nhiều records
     * PUT /updateList
     * Body: List<DTO> (phải có ID)
     */
    @SuppressWarnings("unchecked")
    @PutMapping("/updateList")
    public ResponseEntity<ResponseDto<String>> updateList(
            HttpServletRequest httpServletRequest, @RequestBody List<Object> dtos) {
        LOG.info("Updating {} records", dtos.size());
        baseService.saveListFromObject(dtos);
        return ResponseConfig.success("Updated " + dtos.size() + " records successfully");
    }

    /**
     * TẠO MỚI hoặc CẬP NHẬT (Universal Save)
     * POST /save
     * Body: DTO (có ID = update, không có ID = create)
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/save")
    public ResponseEntity<ResponseDto<Object>> save(
            HttpServletRequest httpServletRequest, @RequestBody Object dto) {
        LOG.info("Saving record (create or update)");
        Object result = baseService.saveFromObject(dto);
        return ResponseConfig.success(result);
    }

    /**
     * TẠO MỚI hoặc CẬP NHẬT nhiều records
     * POST /saveList
     * Body: List<DTO>
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/saveList")
    public ResponseEntity<ResponseDto<String>> saveList(
            HttpServletRequest httpServletRequest, @RequestBody List<Object> dtos) {
        LOG.info("Saving {} records", dtos.size());
        baseService.saveListFromObject(dtos);
        return ResponseConfig.success("Saved " + dtos.size() + " records successfully");
    }

    // ========================================
    // DELETE APIs - XÓA
    // ========================================

    /**
     * XÓA một record theo ID
     * DELETE /{id}
     * PathVariable: id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteById(
            HttpServletRequest httpServletRequest, @PathVariable("id") Long id) {
        LOG.info("Deleting record with ID: {}", id);
        baseService.deleteById(id);
        return ResponseConfig.success("Deleted record ID: " + id);
    }

    /**
     * XÓA nhiều records theo danh sách IDs
     * DELETE /deleteByIds
     * Body: [1, 2, 3, 4]
     */
    @DeleteMapping("/deleteByIds")
    public ResponseEntity<ResponseDto<String>> deleteByIds(
            HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        LOG.info("Deleting {} records", ids.size());
        baseService.deleteByIds(ids);
        return ResponseConfig.success("Deleted " + ids.size() + " records");
    }

    /**
     * XÓA nhiều records (POST method - alternative)
     * POST /deleteList
     * Body: [1, 2, 3, 4]
     */
    @PostMapping("/deleteList")
    public ResponseEntity<ResponseDto<String>> deleteList(
            HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        LOG.info("Deleting {} records (POST method)", ids.size());
        baseService.deleteByIds(ids);
        return ResponseConfig.success("Deleted " + ids.size() + " records");
    }

    // ========================================
    // READ/FIND APIs - TÌM KIẾM & ĐỌC DỮ LIỆU
    // ========================================

    /**
     * TÌM một record theo ID
     * GET /{id}
     * PathVariable: id
     * Response: DTO object hoặc 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> findById(
            HttpServletRequest httpServletRequest, @PathVariable("id") Long id) {
        LOG.info("Finding record with ID: {}", id);
        List results = baseService.findByIds(List.of(id));
        if (results.isEmpty()) {
            LOG.warn("Record not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseConfig.success(results.get(0));
    }

    /**
     * TÌM nhiều records theo danh sách IDs
     * GET /findByIds?ids=1&ids=2&ids=3
     * QueryParam: ids (multiple values)
     * Response: List<DTO>
     */
    @GetMapping("/findByIds")
    public ResponseEntity<ResponseDto<List>> findByIds(
            HttpServletRequest httpServletRequest, @RequestParam List<Long> ids) {
        LOG.info("Finding records with IDs: {}", ids);
        return ResponseConfig.success(baseService.findByIds(ids));
    }

    /**
     * LẤY TẤT CẢ records (không phân trang, không filter)
     * GET /all
     * Response: List<DTO> - Toàn bộ dữ liệu
     * ⚠️ Cẩn thận: Có thể trả về nhiều dữ liệu
     */
    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List>> findAll(
            HttpServletRequest httpServletRequest) {
        LOG.info("Finding all records");
        return ResponseConfig.success(baseService.findAll());
    }

    /**
     * TÌM KIẾM có phân trang, filter, sort
     * POST /search
     * Body: RequestDto {page, size, sorts, filters}
     * Response: Page<DTO> - Có phân trang
     */
    @PostMapping("/search")
    public ResponseEntity<ResponseDto<Page>> search(
            HttpServletRequest httpServletRequest, @RequestBody RequestDto requestDto) {
        LOG.info("Searching records - Page: {}, Size: {}",
                requestDto.getPage(), requestDto.getSize());
        return ResponseConfig.success(baseService.findAll(requestDto));
    }

    /**
     * LẤY DANH SÁCH DROPDOWN (không phân trang, có filter, có sort)
     * POST /dropdown
     * Body: RequestDto {sorts, filters} - không cần page/size
     * Response: List<DTO> - Toàn bộ kết quả theo filter
     * Use case: Populate select/dropdown trong form
     */
    @PostMapping("/dropdown")
    public ResponseEntity<ResponseDto<List>> dropdown(
            HttpServletRequest httpServletRequest, @RequestBody RequestDto requestDto) {
        LOG.info("Getting dropdown list");
        return ResponseConfig.success(baseService.findAllNonePage(requestDto));
    }

    /**
     * ĐẾM số lượng records (có filter)
     * POST /count
     * Body: RequestDto {filters}
     * Response: Long - Số lượng records
     */
    @PostMapping("/count")
    public ResponseEntity<ResponseDto<Long>> count(
            HttpServletRequest httpServletRequest, @RequestBody(required = false) RequestDto requestDto) {
        LOG.info("Counting records");
        // Implement count logic if needed
        return ResponseConfig.success(0L);
    }

    // ========================================
    // UTILITY APIs - TIỆN ÍCH
    // ========================================

    /**
     * KIỂM TRA record có tồn tại không
     * GET /exists/{id}
     * PathVariable: id
     * Response: Boolean
     */
    @GetMapping("/exists/{id}")
    public ResponseEntity<ResponseDto<Boolean>> exists(
            HttpServletRequest httpServletRequest, @PathVariable("id") Long id) {
        LOG.info("Checking if record exists with ID: {}", id);
        List results = baseService.findByIds(List.of(id));
        return ResponseConfig.success(!results.isEmpty());
    }
}
