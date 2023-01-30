package com.example.webecom.mapper;

import com.example.webecom.dto.request.AttributeRequestDTO;
import com.example.webecom.entities.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "value", source = "dto.value")
  Attribute attributeRequestDTOtoAttribute(AttributeRequestDTO dto);
}
