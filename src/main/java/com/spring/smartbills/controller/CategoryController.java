package com.spring.smartbills.controller;

import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Category;
import com.spring.smartbills.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<ResponseDto> createController(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}
