package com.example.webecom.repositories;

import com.example.webecom.entities.Discount;
import com.example.webecom.entities.ProductDiscount;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DiscountRepository extends JpaRepository<Discount, Long> {
  @Query(value = "select d.percent from tbl_discount d inner join  tbl_product_discount pd on d.id = pd.discount_id where pd.product_id = :productId and :date between d.start_date and d.end_date", nativeQuery = true)
  Optional<Integer> findPercentByProductId(@Param("productId") Long productId, @Param("date")Date date);
}
