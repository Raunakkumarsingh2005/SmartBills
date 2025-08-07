package com.spring.smartbills.service;

import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface CategoryService {
    ResponseEntity<ResponseDto> createCategory(Category category);
}
