package com.example.webecom.services.implement;

import com.example.webecom.dto.response.CartResponseDTO;
import com.example.webecom.entities.Cart;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.CartMapper;
import com.example.webecom.repositories.CartDetailRepository;
import com.example.webecom.repositories.CartRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.CartService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
  @Autowired private CartRepository cartRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private CartDetailRepository cartDetailRepository;
  private final CartMapper cartMapper = Mappers.getMapper(CartMapper.class);

  @Override
  public ResponseEntity<CartResponseDTO> getCartByUser(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
    Cart cart = cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user ID = " + userId));
    CartResponseDTO cartResponseDTO = cartMapper.cartToCartResponseDTO(cart);
    int totalProduct = cartDetailRepository.findCartDetailByCart(cart).size();
    cartResponseDTO.setAmount(totalProduct);
    return ResponseEntity.status(HttpStatus.OK).body(cartResponseDTO);
  }
}
