package com.example.webecom.services;

import com.example.webecom.dto.response.CartResponseDTO;
import com.example.webecom.entities.Cart;
import org.springframework.http.ResponseEntity;

public interface CartService {
  ResponseEntity<CartResponseDTO> getCartByUser(Long userId);
}
