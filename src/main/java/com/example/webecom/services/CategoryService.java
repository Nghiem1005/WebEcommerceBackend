package com.example.webecom.services;

import com.example.webecom.dto.request.CategoryRequestDTO;
import com.example.webecom.dto.response.CategoryResponseDTO;

import com.example.webecom.dto.response.ResponseObject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> getAllCategoryOnTrading(Pageable pageable);

    ResponseEntity<?> getAllCategory();

    ResponseEntity<ResponseObject> createCategory(CategoryRequestDTO categoryRequestDTO);

    ResponseEntity<ResponseObject> updateCategory(CategoryRequestDTO categoryRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteCategory(Long id);

    CategoryResponseDTO getCategoryById(Long id);

}