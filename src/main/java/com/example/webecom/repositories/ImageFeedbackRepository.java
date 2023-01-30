package com.example.webecom.repositories;

import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.ImageFeedback;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFeedbackRepository extends JpaRepository<ImageFeedback, Long> {
  List<ImageFeedback> findImageFeedbackByFeedback(Feedback feedback);
}
