package com.spring.smartbills.service.impl;

import com.spring.smartbills.contants.ResponseContants;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Category;
import com.spring.smartbills.exception.ResourceAlreadyExistsException;
import com.spring.smartbills.repository.CategoryRepository;
import com.spring.smartbills.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<ResponseDto> createCategory(Category category) {
        Optional<Category> category1 = categoryRepository.findByCategoryName(category.getCategoryName());
        if (category1.isPresent()) {
            throw new ResourceAlreadyExistsException("Category already exists");
        }

        else {
            categoryRepository.save(category);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(ResponseContants.STATUS_200,ResponseContants.MESSAGE_200));
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
