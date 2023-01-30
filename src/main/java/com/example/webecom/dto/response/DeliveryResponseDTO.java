package com.example.webecom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponseDTO {
  private Long id;
  private String status;
  private Long billId;
  private Long shipperId;
  private String shipperPhone;
  private String shipperName;
}