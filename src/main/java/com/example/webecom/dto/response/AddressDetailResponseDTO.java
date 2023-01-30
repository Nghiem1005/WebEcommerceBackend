package com.example.webecom.dto.response;

import com.example.webecom.entities.Address;
import com.example.webecom.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetailResponseDTO {
  private Long user;
  private Address address;
  private boolean defaultAddress;
}
