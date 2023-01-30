package com.example.webecom.services;

import com.example.webecom.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface CartDetailService {
  ResponseEntity<ResponseObject> addProductToCart(Long productId, int amount, String token);
  ResponseEntity<ResponseObject> deleteProductToCart(Long productId, String token);
  ResponseEntity<?> getProductToCart(String token);
}
