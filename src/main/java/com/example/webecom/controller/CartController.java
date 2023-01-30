package com.example.webecom.controller;

import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.CartDetailService;
import com.example.webecom.services.CartService;
import com.example.webecom.utils.JwtTokenUtil;
import com.example.webecom.utils.Utils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/cart")
public class CartController {
  @Autowired private CartService cartService;
  @Autowired private CartDetailService cartDetailService;

  @GetMapping(value = "/user")
  public ResponseEntity<?> getCartByUser(@RequestParam(name = "userId") Long userId){
    return cartService.getCartByUser(userId);
  }

  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> addProductToCart(@RequestParam(name = "productId") Long productId, @RequestParam(name = "amount") int amount, HttpServletRequest request){
    return cartDetailService.addProductToCart(productId, amount, JwtTokenUtil.getAccessToken(request));
  }

  @DeleteMapping(value = "/product")
  public ResponseEntity<ResponseObject> deleteProductToCart(@RequestParam(name = "productId") Long productId, HttpServletRequest request){
    return cartDetailService.deleteProductToCart(productId, JwtTokenUtil.getAccessToken(request));
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getProductInCart(HttpServletRequest request){
    return cartDetailService.getProductToCart(JwtTokenUtil.getAccessToken(request));
  }
}
