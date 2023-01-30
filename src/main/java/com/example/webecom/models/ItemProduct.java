package com.example.webecom.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemProduct {
  private Long id;
  private String name;
  private int quantity;
  private BigDecimal price;
  private String thumbnail;
  private double discount;
  private BigDecimal pricePayed;
}
