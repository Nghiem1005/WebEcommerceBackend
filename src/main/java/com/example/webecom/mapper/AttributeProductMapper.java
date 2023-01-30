package com.example.webecom.mapper;

import com.example.webecom.dto.response.AttributeProductResponseDTO;
import com.example.webecom.entities.Attribute;
import com.example.webecom.entities.AttributeProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeProductMapper {
  @Mapping(target = "productId", source = "a.product.id")
  @Mapping(target = "name", source = "a.product.name")
  @Mapping(target = "attribute", source = "a.attribute")
  AttributeProductResponseDTO attributeToAttributeProductResponseDTO(AttributeProduct a);
}
