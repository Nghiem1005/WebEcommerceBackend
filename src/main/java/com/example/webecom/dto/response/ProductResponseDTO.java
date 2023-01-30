package com.example.webecom.dto.response;

import com.example.webecom.entities.Brand;
import com.example.webecom.entities.Category;
import com.example.webecom.entities.Discount;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
  private Long id;
  private String name;
  private int amount;
  private BigDecimal standCost;
  private String description;
  private String thumbnail;
  private Long categoryId;
  private String category;
  private Long brandId;
  private String brand;
  private String[] images;
  private Discount discount;
  private int vote;
}
