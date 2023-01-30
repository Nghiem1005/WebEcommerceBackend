package com.example.webecom.repositories;

import com.example.webecom.entities.Brand;
import com.example.webecom.entities.Category;
import com.example.webecom.entities.Product;
import com.example.webecom.models.IProductVote;
import com.example.webecom.models.IStatisticDay;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
    JpaSpecificationExecutor<Product> {
  Optional<Product> findProductByName(String name);

  @Query(value = "select product_id as productId, avg(vote) as avgVote from tbl_feedback group by product_id order by avgVote desc limit 10", nativeQuery = true)
  List<IProductVote> getTop10ProductByVote();

  List<Product> findProductByBrand(Brand brand);

  Page<Product> findProductByBrand(Brand brand, Pageable pageable);

  List<Product> findProductByCategory(Category category);

  Page<Product> findProductByCategory(Category category, Pageable pageable);

  @Query(value = "select count(p.id) from Product p")
  Optional<Integer> getNumberOfProduct();

  @Query(value = "select weekday(create_date) as weekDay, count(id) as totalValue  from tbl_user "
      + "where create_date <= current_date() and create_date > date_sub(current_date(), interval 7 Day) "
      + "group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findAllUserByDay();

}
