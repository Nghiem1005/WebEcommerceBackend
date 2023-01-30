package com.example.webecom.entities;

import com.example.webecom.entities.Keys.BillDetailKey;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bill_detail")
@IdClass(BillDetailKey.class)
public class BillDetail {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "bill_id")
  private Bill bill;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  private int quantity;
}
