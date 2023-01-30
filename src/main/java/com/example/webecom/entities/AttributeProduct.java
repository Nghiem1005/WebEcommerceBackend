package com.example.webecom.entities;

import com.example.webecom.entities.Keys.AttributeProductKey;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_attribute_product")
@IdClass(AttributeProductKey.class)
public class AttributeProduct {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "attribute_id")
  private Attribute attribute;
}
