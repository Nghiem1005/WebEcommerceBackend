package com.example.webecom.dto.response;

import com.example.webecom.entities.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeProductResponseDTO {
  private Long productId;
  private String name;
  private Attribute attribute;
}
