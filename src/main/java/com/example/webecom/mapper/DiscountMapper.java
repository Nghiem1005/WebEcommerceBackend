package com.example.webecom.mapper;

import com.example.webecom.dto.request.DiscountRequestDTO;
import com.example.webecom.dto.response.DiscountResponseDTO;
import com.example.webecom.entities.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "title", source = "dto.title")
  @Mapping(target = "description", source = "dto.description")
  @Mapping(target = "percent", source = "dto.percent")
  @Mapping(target = "code", source = "dto.code")
  @Mapping(target = "startDate", source = "dto.startDate")
  @Mapping(target = "endDate", source = "dto.endDate")
  Discount discountRequestDTOtoDiscount(DiscountRequestDTO dto);

  @Mapping(target = "id", source = "d.id")
  @Mapping(target = "title", source = "d.title")
  @Mapping(target = "description", source = "d.description")
  @Mapping(target = "percent", source = "d.percent")
  @Mapping(target = "code", source = "d.code")
  @Mapping(target = "startDate", source = "d.startDate")
  @Mapping(target = "endDate", source = "d.endDate")
  DiscountResponseDTO discountToDiscountResponseDTO(Discount d);
}
