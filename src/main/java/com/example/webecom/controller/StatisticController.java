package com.example.webecom.controller;

import com.example.webecom.services.BillService;
import com.example.webecom.services.ProductService;
import com.example.webecom.services.StatisticService;
import com.example.webecom.services.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/statistic")
public class StatisticController {
  @Autowired
  private ProductService productService;

  @Autowired
  private UserService userService;

  @Autowired
  private BillService billService;

  @Autowired private StatisticService statisticService;

  @GetMapping(value = "/product")
  public ResponseEntity<Integer> getNumberOfProduct() {
    return productService.getNumberOfProduct();
  }

  @GetMapping(value = "/user")
  public ResponseEntity<Integer> getNumberOfUser() {
    return userService.getNumberOfUser();
  }

  @GetMapping(value = "/bill")
  public ResponseEntity<Integer> getNumberOfBill() {
    return billService.getNumberOfBill();
  }

  @GetMapping(value = "/sales")
  public ResponseEntity<Double> getSales() {
    return billService.getSales();
  }

  @GetMapping(value = "/user/day")
  public ResponseEntity<?> getUserByDay() {
    return statisticService.getAllUserByDay();
  }

  @GetMapping(value = "/bill/day")
  public ResponseEntity<?> getRevenueByDay() {
    return statisticService.getRevenueByDay();
  }

  @GetMapping(value = "/bill/product")
  public ResponseEntity<?> getNumberProductOfAllBill() {
    return statisticService.getNumberProductOfAllBill();
  }

  @GetMapping(value = "/bill/product/sales")
  public ResponseEntity<?> getNumberProductOfBill() {
    return statisticService.getNumberProductOfBill();
  }

  @GetMapping(value = "/bill/product/sales/week/{productId}")
  public ResponseEntity<?> getNumberProductOfBillInThreeWeek(@PathVariable(name = "productId") Long productId) {
    return statisticService.getNumberProductOfBillInThreeWeek(productId);
  }
}
