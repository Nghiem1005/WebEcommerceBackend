package com.example.webecom.repositories;

import com.example.webecom.entities.Attribute;
import com.example.webecom.entities.AttributeProduct;
import com.example.webecom.entities.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeProductRepository extends JpaRepository<AttributeProduct, Long> {
  List<AttributeProduct> findAttributeProductByAttribute(Attribute attribute);

  Optional<AttributeProduct> findAttributeProductByAttributeAndProduct(Attribute attribute, Product product);

  List<AttributeProduct> findAttributeProductByProduct(Product product);

}
