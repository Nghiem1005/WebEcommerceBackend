package com.example.webecom.dto.request;

import com.example.webecom.entities.User;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {
  private String province;
  private String district;
  private String ward;
  private String apartmentNumber;
  private boolean defaultAddress;
  private int codeArea;
}
