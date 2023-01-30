package com.example.webecom.dto.request;

import com.example.webecom.models.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillRequestDTO {
  private String status;
  @NotNull(message="An payment method is required!")
  private String paymentMethod;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date payDate;
  List<Item> items;
  private BigDecimal feeDelivery;
}
