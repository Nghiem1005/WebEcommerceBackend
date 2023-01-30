package com.example.webecom.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailResponseDTO {
  private Long productId;
  private String productName;
  private String productThumbnail;
  private Long billId;
  private int amount;
  private Date payDate;
  private String payMethod;
  private String status;
  private BigDecimal price;
  private UserResponseDTO userResponseDTO;
}
