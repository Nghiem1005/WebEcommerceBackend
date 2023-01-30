package com.example.webecom.entities;

import com.example.webecom.entities.Keys.CartDetailKey;
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
@Table(name = "tbl_cart_detail")
@IdClass(CartDetailKey.class)
public class CartDetail {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @NotNull(message = "Cart detail amount is required")
  private int amount;
}
