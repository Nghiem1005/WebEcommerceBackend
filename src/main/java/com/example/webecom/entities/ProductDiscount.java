package com.example.webecom.entities;

import com.example.webecom.entities.Keys.ProductDiscountKey;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product_discount")
@IdClass(ProductDiscountKey.class)
public class ProductDiscount {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "discount_id")
  private Discount discount;
}
