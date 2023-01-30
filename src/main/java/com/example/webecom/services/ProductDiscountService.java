package com.example.webecom.services;

import com.example.webecom.dto.response.ProductDiscountResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.ProductDiscount;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ProductDiscountService {
  ResponseEntity<ResponseObject> createProductDiscount(Long productId, Long discountId);
  ResponseEntity<?> getProductDiscount(Long productId);
  ResponseEntity<?> getProductDiscountCurrentByProduct(Long productId);

  ResponseEntity<?> getProductDiscountCurrent();
  ResponseEntity<ResponseObject> deleteProductDiscount(Long productId, Long discountId);
}
