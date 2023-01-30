package com.example.webecom.services.implement;

import com.example.webecom.dto.request.FeedbackRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.FeedbackResponseDTO;
import com.example.webecom.entities.Role;
import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.ImageFeedback;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.FeedbackMapper;
import com.example.webecom.models.ItemTotalPage;
import com.example.webecom.repositories.RoleRepository;
import com.example.webecom.repositories.FeedbackRepository;
import com.example.webecom.repositories.ImageFeedbackRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.FeedbackService;

import com.example.webecom.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
  private final FeedbackMapper mapper = Mappers.getMapper(FeedbackMapper.class);

  @Autowired
  private FeedbackRepository feedbackRepository;
  @Autowired
  private ImageStorageService imageStorageService;
  @Autowired
  private ImageFeedbackRepository imageFeedbackRepository;
  @Autowired
  private ProductRepository productRepository;

  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<?> getAllFeedbackOnTrading(Pageable pageable) {
    Page<Feedback> getFeedbackList = feedbackRepository.findAll(pageable);
    List<Feedback> feedbackList = getFeedbackList.getContent();
    List<FeedbackResponseDTO> feedbackResponseDTOList = new ArrayList<>();
    for (Feedback feedback : feedbackList) {
      FeedbackResponseDTO feedbackResponseDTO = mapper.feedbackToFeedbackResponseDTO(feedback);
      feedbackResponseDTOList.add(feedbackResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(feedbackResponseDTOList,
        getFeedbackList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getAllFeedbackByProduct(Pageable pageable, Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));

    Page<Feedback> getFeedbackList = feedbackRepository.findFeedbackByProduct(pageable, product);
    List<Feedback> feedbackList = getFeedbackList.getContent();
    List<FeedbackResponseDTO> feedbackResponseDTOList = new ArrayList<>();
    for (Feedback feedback : feedbackList) {
      FeedbackResponseDTO feedbackResponseDTO = mapper.feedbackToFeedbackResponseDTO(feedback);
      feedbackResponseDTOList.add(feedbackResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(feedbackResponseDTOList,
        getFeedbackList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createFeedback(FeedbackRequestDTO feedbackRequestDTO) {
    Feedback feedback = mapper.feedbackRequestDTOtoFeedback(feedbackRequestDTO);

    User user = userRepository.findById(feedbackRequestDTO.getUser()).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + feedbackRequestDTO.getUser()));
    Product product = productRepository.findById(feedbackRequestDTO.getProduct()).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + feedbackRequestDTO.getProduct()));

    feedback.setProduct(product);
    feedback.setUser(user);

    Feedback feedbackSaved = feedbackRepository.save(feedback);
    FeedbackResponseDTO feedbackResponseDTO = mapper.feedbackToFeedbackResponseDTO(feedbackSaved);

    if (feedbackRequestDTO.getImages() != null) {
      feedbackResponseDTO.setImages(saveImage(feedbackRequestDTO, feedbackSaved));
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create feedback successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateFeedback(FeedbackRequestDTO feedbackRequestDTO, Long id) {
    Feedback feedback = mapper.feedbackRequestDTOtoFeedback(feedbackRequestDTO);
    Feedback getFeedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    feedback.setId(id);
    Feedback feedbackSaved = feedbackRepository.save(feedback);
    FeedbackResponseDTO feedbackResponseDTO = mapper.feedbackToFeedbackResponseDTO(feedbackSaved);

    // Save image
    if (feedbackRequestDTO.getImages() != null) {
      List<ImageFeedback> imageFeedbackList = imageFeedbackRepository.findImageFeedbackByFeedback(feedbackSaved);
      for (ImageFeedback imageFeedback : imageFeedbackList) {
        imageStorageService.deleteFile(imageFeedback.getPath(), "feedback/images");
        imageFeedbackRepository.delete(imageFeedback);
      }
      feedbackResponseDTO.setImages(saveImage(feedbackRequestDTO, feedbackSaved));
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update feedback successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteFeedback(Long id) {
    Feedback getFeedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    // Delete image of feedback
    List<ImageFeedback> imageFeedbackList = imageFeedbackRepository.findImageFeedbackByFeedback(getFeedback);
    for (ImageFeedback imageFeedback : imageFeedbackList) {
      imageStorageService.deleteFile(imageFeedback.getPath(), "feedback/images");
      imageFeedbackRepository.delete(imageFeedback);
    }
    feedbackRepository.delete(getFeedback);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete feedback successfully!"));
  }

  @Override
  public FeedbackResponseDTO getFeedbackById(Long id) {
    Feedback feedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    FeedbackResponseDTO feedbackResponseDTO = mapper.feedbackToFeedbackResponseDTO(feedback);

    return feedbackResponseDTO;
  }

  private String[] saveImage(FeedbackRequestDTO feedbackRequestDTO, Feedback feedback) {
    int numberOfFile = feedbackRequestDTO.getImages().length;
    String[] images = new String[numberOfFile];
    for (int i = 0; i < numberOfFile; i++) {
      images[i] = imageStorageService.storeFile(feedbackRequestDTO.getImages()[i], "feedback/images");
    }

    for (String path : images) {
      ImageFeedback imageFeedback = new ImageFeedback();
      imageFeedback.setFeedback(feedback);
      imageFeedback.setPath(path);
      this.imageFeedbackRepository.save(imageFeedback);
    }
    return images;
  }

}
