package com.example.webecom.entities.Keys;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetailKey implements Serializable {
  private Long user;
  private Long address;

  @Override
  public int hashCode() {
    return Objects.hash(getUser(), getAddress());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof ProductDiscountKey)) return false;
    AddressDetailKey that = (AddressDetailKey) obj;
    return getAddress().equals(that.address) && getUser().equals(that.user);
  }
}
