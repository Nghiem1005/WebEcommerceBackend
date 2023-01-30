package com.example.webecom.dto.response;

import com.example.webecom.entities.Brand;
import com.example.webecom.entities.Category;
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
public class DiscountResponseDTO {
  private Long id;
  private String title;
  private String description;
  private double percent;
  private String code;
  private Date startDate;
  private Date endDate;
}
