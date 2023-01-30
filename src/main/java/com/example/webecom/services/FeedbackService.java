package com.example.webecom.services;

import com.example.webecom.dto.request.FeedbackRequestDTO;
import com.example.webecom.dto.response.FeedbackResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FeedbackService {
    ResponseEntity<?> getAllFeedbackOnTrading(Pageable pageable);

    ResponseEntity<?> getAllFeedbackByProduct(Pageable pageable, Long productId);

    ResponseEntity<ResponseObject> createFeedback(FeedbackRequestDTO feedbackRequestDTO);

    ResponseEntity<ResponseObject> updateFeedback(FeedbackRequestDTO feedbackRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteFeedback(Long id);

    FeedbackResponseDTO getFeedbackById(Long id);

}