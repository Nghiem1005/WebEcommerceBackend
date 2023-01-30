package com.example.webecom.mapper;

import com.example.webecom.dto.request.ProductRequestDTO;
import com.example.webecom.dto.response.ProductResponseDTO;
import com.example.webecom.dto.response.ProductStatisticResponseDTO;
import com.example.webecom.entities.Product;
import com.example.webecom.models.IProductQuantity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "description", source = "dto.description")
  @Mapping(target = "amount", source = "dto.amount")
  @Mapping(target = "standCost", source = "dto.standCost")
  @Mapping(target = "thumbnail", expression = "java(null)")
  @Mapping(target = "category.id", source = "dto.category")
  @Mapping(target = "brand.id", source = "dto.brand")
  Product productRequestDTOtoProduct(ProductRequestDTO dto);

  @Mapping(target = "id", source = "p.id")
  @Mapping(target = "name", source = "p.name")
  @Mapping(target = "amount", source = "p.amount")
  @Mapping(target = "standCost", source = "p.standCost")
  @Mapping(target = "description", source = "p.description")
  @Mapping(target = "thumbnail", source = "p.thumbnail")
  @Mapping(target = "category", source = "p.category.name")
  @Mapping(target = "brand", source = "p.brand.name")
  @Mapping(target = "brandId", source = "p.brand.id")
  @Mapping(target = "categoryId", source = "p.category.id")
  ProductResponseDTO productToProductResponseDTO(Product p);

  @Mapping(target = "id", source = "p.product.id")
  @Mapping(target = "name", source = "p.product.name")
  @Mapping(target = "quantitySales", source = "p.quantity")
  @Mapping(target = "standCost", source = "p.product.standCost")
  @Mapping(target = "amount", source = "p.product.amount")
  @Mapping(target = "totalPrice", expression = "java(null)")
  ProductStatisticResponseDTO productToProductStatisticResponseDTO(IProductQuantity p);
}
