package com.example.webecom.services;

import com.example.webecom.dto.request.BrandRequestDTO;
import com.example.webecom.dto.response.BrandResponseDTO;

import com.example.webecom.dto.response.ResponseObject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BrandService {
    ResponseEntity<?> getAllBrandOnTrading(Pageable pageable);
    ResponseEntity<?> getAllBrand();

    ResponseEntity<ResponseObject> createBrand(BrandRequestDTO brandRequestDTO);

    ResponseEntity<ResponseObject> updateBrand(BrandRequestDTO brandRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteBrand(Long id);

    BrandResponseDTO getBrandById(Long id);

}