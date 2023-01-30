package com.example.webecom.dto.response;

import com.example.webecom.entities.Discount;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGalleryDTO {
  private Long id;
  private String name;
  private BigDecimal StandCost;
  private BigDecimal price;
  private String thumbnail;
  private Discount discount;
  private double vote;
}
