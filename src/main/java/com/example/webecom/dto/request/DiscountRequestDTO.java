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
public class DiscountRequestDTO {
  private Long id;
  @Column(length = 45)
  @NotNull(message = "Discount title is required")
  private String title;

  private String description;

  @NotNull(message = "Discount code is required")
  @Min(value = 1, message = "Percent must be greater 1")
  @Max(value = 99, message = "Percent must smaller 99")
  private double percent;

  @NotNull(message = "Discount code is required")
  @Size(max = 10, min = 5, message = "Invalid code size")
  private String code;
  private Date startDate;
  private Date endDate;
}
