package com.example.webecom.controller;

import com.example.webecom.dto.request.FeedbackRequestDTO;
import com.example.webecom.dto.response.FeedbackResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.FeedbackService;
import com.example.webecom.utils.Utils;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/feedback")
public class FeedbackController {
  @Autowired
  private FeedbackService feedbackService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllFeedbackOnTrading(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedbackOnTrading(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createFeedback(@ModelAttribute @Valid FeedbackRequestDTO feedbackRequestDTO) {
    return feedbackService.createFeedback(feedbackRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateFeedback(@ModelAttribute @Valid FeedbackRequestDTO feedbackRequestDTO,
      @RequestParam(name = "id") Long id) {
    return feedbackService.updateFeedback(feedbackRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteFeedback(@RequestParam(name = "id") Long id) {
    return feedbackService.deleteFeedback(id);
  }

  @GetMapping(value = "/{id}")
  public FeedbackResponseDTO getFeedbackById(@PathVariable(name = "id") Long id) {
    return feedbackService.getFeedbackById(id);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getAllFeedbackByProduct(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "productId") Long productId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedbackByProduct(pageable, productId);
  }
}
