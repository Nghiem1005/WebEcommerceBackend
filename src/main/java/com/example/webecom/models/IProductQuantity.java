package com.example.webecom.models;

import com.example.webecom.entities.Bill;
import com.example.webecom.entities.Product;

public interface IProductQuantity {
  Product getProduct();
  int getQuantity();
}
