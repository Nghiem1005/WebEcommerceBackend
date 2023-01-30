package com.example.webecom.dto.request;

import com.example.webecom.entities.Category;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
  @Size(message="Invalid size.", max = 64, min=1)
  private String name;
  @Min(value = 1, message = "Amount must be greater 1")
  private int amount;
  @Min(value = 1, message = "Stand cost must be greater 1")
  private BigDecimal standCost;
  private String description;
  private MultipartFile thumbnail;
  private Long category;
  private Long brand;
  private MultipartFile[] images;
}
