package com.example.webecom.repositories;

import com.example.webecom.entities.Cart;
import com.example.webecom.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findCartByUser(User user);
}
