package com.example.webecom.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tbl_address")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Address province is required")
  private String province;

  @NotNull(message = "Address district is required")
  private String district;

  @NotNull(message = "Address ward is required")
  private String ward;

  @NotNull(message = "Address apartment number is required")
  private String apartmentNumber;

  @NotNull(message = "Code area is required")
  private int codeArea;
}
