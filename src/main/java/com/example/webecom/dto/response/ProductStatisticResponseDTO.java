package com.example.webecom.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisticResponseDTO {
  private Long id;
  private String name;
  private int amount;
  private BigDecimal standCost;
  private int quantitySales;
  private BigDecimal totalPrice;
}
