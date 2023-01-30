package com.example.webecom.services.implement;

import com.example.webecom.dto.response.CartDetailResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Cart;
import com.example.webecom.entities.CartDetail;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.InvalidValueException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.CartDetailMapper;
import com.example.webecom.repositories.CartDetailRepository;
import com.example.webecom.repositories.CartRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.services.CartDetailService;
import com.example.webecom.utils.JwtTokenUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CartDetailServiceImpl implements CartDetailService {
  @Autowired
  AuthenticationManager auth;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired private CartDetailRepository cartDetailRepository;
  @Autowired private CartRepository cartRepository;
  @Autowired private ProductRepository productRepository;
  private final CartDetailMapper cartDetailMapper = Mappers.getMapper(CartDetailMapper.class);

  @Override
  public ResponseEntity<ResponseObject> addProductToCart(Long productId, int amount, String token) {
    User user = getUserToken(token);
    CartDetail cartDetail = new CartDetail();
    Cart cart = cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user id = " + user.getId()));
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    if (amount > product.getAmount()){
      throw new InvalidValueException("Amount product add to cart must be less than amount product exists");
    }
    Optional<CartDetail> getCartDetail = cartDetailRepository.findCartDetailByCartAndProduct(cart, product);
    if (getCartDetail.isPresent()){
      if (amount < 0){
        throw new InvalidValueException("Amount product must greater than 1");
      }
      cartDetail = getCartDetail.get();

    } else {
      cartDetail.setCart(cart);
      cartDetail.setProduct(product);
    }
    cartDetail.setAmount(amount);
    CartDetail cartDetailSaved = cartDetailRepository.save(cartDetail);
    CartDetailResponseDTO cartDetailResponseDTO = cartDetailMapper.cartDetailToCartDetailResponseDTO(cartDetailSaved);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add product to cart success!", cartDetailResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteProductToCart(Long productId, String token) {
    User user = getUserToken(token);
    Cart cart = cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user id = " + user.getId()));
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    CartDetail cartDetail = cartDetailRepository.findCartDetailByCartAndProduct(cart, product).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId + " and user ID = " + user.getId()));
    cartDetailRepository.delete(cartDetail);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete product in cart success!"));
  }

  @Override
  public ResponseEntity<?> getProductToCart(String token) {
    User user = getUserToken(token);
    Cart cart = cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user id = " + user.getId()));
    List<CartDetail> cartDetailList = cartDetailRepository.findCartDetailByCart(cart);
    List<CartDetailResponseDTO> cartDetailResponseDTOList = new ArrayList<>();
    for (CartDetail cartDetail : cartDetailList){
      CartDetailResponseDTO cartDetailResponseDTO = cartDetailMapper.cartDetailToCartDetailResponseDTO(cartDetail);
      cartDetailResponseDTOList.add(cartDetailResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(cartDetailResponseDTOList);
  }

  private User getUserToken(String token){
    User user = (User) jwtTokenUtil.getUserDetails(token);
    return user;
  }
}
