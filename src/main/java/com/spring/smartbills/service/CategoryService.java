package com.spring.smartbills.service;

import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    ResponseEntity<ResponseDto> createCategory(Category category);
    ResponseEntity<List<Category>> getAllCategories();
}
