package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.response.CategoryResponse;
import com.winmart.userservice.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        super(categoryService);
        this.categoryService = categoryService;
    }

    /**
     * Get all root categories (categories without parent)
     * GET /api/categories/roots
     */
    @GetMapping("/roots")
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getRootCategories() {
        List<CategoryResponse> rootCategories = categoryService.getRootCategories();
        return ResponseConfig.success(rootCategories);
    }

    /**
     * Get full category tree (hierarchical structure)
     * GET /api/categories/tree
     */
    @GetMapping("/tree")
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getCategoryTree() {
        List<CategoryResponse> tree = categoryService.getCategoryTree();
        return ResponseConfig.success(tree);
    }

    /**
     * Get children of a specific category
     * GET /api/categories/{parentId}/children
     */
    @GetMapping("/{parentId}/children")
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getChildren(
            @PathVariable("parentId") Long parentId) {
        List<CategoryResponse> children = categoryService.getChildren(parentId);
        return ResponseConfig.success(children);
    }
}
