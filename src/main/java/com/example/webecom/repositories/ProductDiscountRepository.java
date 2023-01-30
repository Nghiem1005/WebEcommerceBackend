package com.example.webecom.repositories;

import com.example.webecom.entities.Discount;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
  Optional<ProductDiscount> findProductDiscountByDiscountAndProduct(Discount discount, Product product);
  List<ProductDiscount> findProductDiscountByProduct(Product product);

  @Query("SELECT pd " +
      "FROM ProductDiscount pd " +
      "WHERE pd.product.id = :id " +
      "AND pd.discount.startDate <= CURRENT_TIMESTAMP " +
      "AND pd.discount.endDate > CURRENT_TIMESTAMP")
  Optional<ProductDiscount> findAllCurrentDiscountByProductId(@Param("id") Long id);

  @Query("SELECT pd " +
      "FROM ProductDiscount pd " +
      "WHERE pd.discount.startDate <= CURRENT_TIMESTAMP " +
      "AND pd.discount.endDate > CURRENT_TIMESTAMP")
  List<ProductDiscount> findAllCurrentProductDiscount();

  @Query("SELECT pd " +
      "FROM ProductDiscount pd " +
      "WHERE pd.discount.startDate <= CURRENT_TIMESTAMP " +
      "AND pd.discount.endDate > CURRENT_TIMESTAMP")
  List<ProductDiscount> findAllCurrentProductDiscount(Sort sort);

  @Query("SELECT pd " +
      "FROM ProductDiscount pd " +
      "WHERE pd.product.id = :id " +
      "AND pd.discount.startDate <= :date " +
      "AND pd.discount.endDate > :date")
  Optional<ProductDiscount> findAllDiscountByProductIdAndDate(@Param("id") Long id, @Param("date") Date date);

}
