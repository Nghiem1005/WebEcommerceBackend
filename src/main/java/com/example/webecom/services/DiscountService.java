package com.example.webecom.services;

import com.example.webecom.dto.request.DiscountRequestDTO;
import com.example.webecom.dto.response.DiscountResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DiscountService {
    ResponseEntity<?> getAllDiscountOnTrading(Pageable pageable);

    ResponseEntity<?> getAllDiscountByProduct(Pageable pageable, Long productId);

    ResponseEntity<ResponseObject> createDiscount(DiscountRequestDTO discountRequestDTO);

    ResponseEntity<ResponseObject> updateDiscount(DiscountRequestDTO discountRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteDiscount(Long id);

    DiscountResponseDTO getDiscountById(Long id);

}