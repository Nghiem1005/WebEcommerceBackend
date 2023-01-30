package com.example.webecom.dto.response;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
  private Long id;
  private String content;
  private double vote;
  private Long user;
  private String userName;
  private Long product;
  private String productName;
  private String productThumbnail;
  private Date createDate;
  private Date updateDate;
  private String[] images;
}