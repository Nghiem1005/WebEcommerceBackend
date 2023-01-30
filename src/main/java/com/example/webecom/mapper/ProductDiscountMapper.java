package com.example.webecom.mapper;

import com.example.webecom.dto.response.ProductDiscountResponseDTO;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDiscountMapper {
  @Mapping(target = "id", source = "p.product.id")
  @Mapping(target = "name", source = "p.product.name")
  @Mapping(target = "discount", source = "p.discount")
  ProductDiscountResponseDTO productDiscountToProductDiscountResponseDTO(ProductDiscount p);
}
