package com.example.webecom.entities.Keys;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeProductKey implements Serializable {
  private Long attribute;
  private Long product;
  @Override
  public int hashCode() {
    return Objects.hash(getAttribute(), getProduct());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof AttributeProductKey)) return false;
    AttributeProductKey that = (AttributeProductKey) obj;
    return getAttribute().equals(that.attribute) && getProduct().equals(that.product);
  }
}
