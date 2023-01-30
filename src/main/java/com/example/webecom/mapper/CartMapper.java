package com.example.webecom.mapper;

import com.example.webecom.dto.response.CartResponseDTO;
import com.example.webecom.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
  @Mapping(target = "userId", source = "c.user.id")
  @Mapping(target = "cartId", source = "c.id")
  CartResponseDTO cartToCartResponseDTO(Cart c);
}
