package com.example.webecom.repositories;

import com.example.webecom.entities.ImageProduct;
import com.example.webecom.entities.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
  List<ImageProduct> findImageProductByProduct(Product product);

}
