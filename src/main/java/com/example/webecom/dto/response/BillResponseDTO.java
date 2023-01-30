package com.example.webecom.dto.response;

import com.example.webecom.models.ItemProduct;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDTO {
  private Long userId;
  private String userName;
  private Long billId;
  private String status;
  private String paymentMethod;
  private BigDecimal totalPrice;
  private Date payDate;
  private ItemProduct[] products;
  private String address;
  private String statusDelivery;
  private Long deliveryId;
  private int deliveryCodeArea;
}
