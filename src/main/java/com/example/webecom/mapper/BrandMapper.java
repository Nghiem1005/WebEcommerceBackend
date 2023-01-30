package com.example.webecom.mapper;

import com.example.webecom.dto.request.BrandRequestDTO;
import com.example.webecom.dto.response.BrandResponseDTO;
import com.example.webecom.entities.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "description", source = "dto.description")
  Brand brandRequestDTOtoBrand(BrandRequestDTO dto);

  @Mapping(target = "id", source = "brand.id")
  @Mapping(target = "name", source = "brand.name")
  @Mapping(target = "description", source = "brand.description")
  BrandResponseDTO brandToBrandResponseDTO(Brand brand);

}