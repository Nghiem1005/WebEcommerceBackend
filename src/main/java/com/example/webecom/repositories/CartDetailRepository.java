package com.example.webecom.repositories;

import com.example.webecom.entities.Cart;
import com.example.webecom.entities.CartDetail;
import com.example.webecom.entities.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
  List<CartDetail> findCartDetailByCart(Cart cart);
  Optional<CartDetail> findCartDetailByCartAndProduct(Cart cart, Product product);
}
