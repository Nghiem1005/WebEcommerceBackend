package com.example.webecom.services;

import com.example.webecom.dto.request.ProductRequestDTO;
import com.example.webecom.dto.response.ProductGalleryDTO;

import com.example.webecom.dto.response.ProductResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Brand;
import com.example.webecom.entities.Category;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
  ResponseEntity<?> getAllProductOnTrading(Pageable pageable);

  ResponseEntity<?> getAllProductByCategory(Long categoryId, Pageable pageable);

  ResponseEntity<?> getAllProductByBrand(Long brandId, Pageable pageable);

  ResponseEntity<ResponseObject> createProduct(ProductRequestDTO productRequestDTO);

  ResponseEntity<ResponseObject> updateProduct(ProductRequestDTO productRequestDTO, Long id);

  ResponseEntity<ResponseObject> deleteProduct(Long id);

  ResponseEntity<?> getProductById(Long id);

  ResponseEntity<?> search(String search, int page, int size);

  ResponseEntity<?> getTop10ProductByVote();

  ResponseEntity<Integer> getNumberOfProduct();

}
