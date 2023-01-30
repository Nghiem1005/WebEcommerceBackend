package com.example.webecom.repositories;

import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.User;
import java.util.List;

import org.springframework.data.domain.Page;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
  Page<Feedback> findFeedbackByProduct(Pageable pageable, Product product);

  List<Feedback> findFeedbacksByProduct(Product product);

  List<Feedback> findFeedbackByUser(User user);

}
