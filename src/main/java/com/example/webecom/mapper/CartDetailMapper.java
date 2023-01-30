package com.example.webecom.mapper;

import com.example.webecom.dto.response.CartDetailResponseDTO;
import com.example.webecom.entities.CartDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartDetailMapper {
  @Mapping(target = "productId", source = "c.product.id")
  @Mapping(target = "cartId", source = "c.cart.id")
  @Mapping(target = "amount", source = "c.amount")
  @Mapping(target = "productName", source = "c.product.name")
  CartDetailResponseDTO cartDetailToCartDetailResponseDTO(CartDetail c);
}
