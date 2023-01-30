package com.example.webecom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailResponseDTO {
  private Long productId;
  private String productName;
  private Long cartId;
  private int amount;
}
