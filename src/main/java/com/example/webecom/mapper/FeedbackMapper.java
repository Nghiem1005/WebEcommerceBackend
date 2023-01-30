package com.example.webecom.mapper;

import com.example.webecom.dto.request.FeedbackRequestDTO;
import com.example.webecom.dto.response.FeedbackResponseDTO;
import com.example.webecom.entities.Feedback;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "content", source = "dto.content")
  @Mapping(target = "vote", source = "dto.vote")
  @Mapping(target = "user", expression = "java(null)")
  @Mapping(target = "product", expression = "java(null)")
  @Mapping(target = "createDate", expression = "java(null)")
  @Mapping(target = "updateDate", expression = "java(null)")
  Feedback feedbackRequestDTOtoFeedback(FeedbackRequestDTO dto);

  @Mapping(target = "id", source = "feedback.id")
  @Mapping(target = "content", source = "feedback.content")
  @Mapping(target = "vote", source = "feedback.vote")
  @Mapping(target = "user", source = "feedback.user.id")
  @Mapping(target = "product", source = "feedback.product.id")
  @Mapping(target = "createDate", source = "feedback.createDate")
  @Mapping(target = "updateDate", source = "feedback.updateDate")
  @Mapping(target = "userName", source = "feedback.user.name")
  @Mapping(target = "productName", source = "feedback.product.name")
  @Mapping(target = "productThumbnail", source = "feedback.product.thumbnail")
  FeedbackResponseDTO feedbackToFeedbackResponseDTO(Feedback feedback);
}