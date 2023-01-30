package com.example.webecom.mapper;

import com.example.webecom.dto.request.CategoryRequestDTO;
import com.example.webecom.dto.response.CategoryResponseDTO;
import com.example.webecom.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  Category categoryRequestDTOtoCategory(CategoryRequestDTO dto);

  @Mapping(target = "id", source = "category.id")
  @Mapping(target = "name", source = "category.name")
  CategoryResponseDTO categoryToCategoryResponseDTO(Category category);

}