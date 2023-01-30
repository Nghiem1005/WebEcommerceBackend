package com.example.webecom.services;

import org.springframework.http.ResponseEntity;

public interface StatisticService {
  ResponseEntity<?> getAllUserByDay();

  ResponseEntity<?> getRevenueByDay();
  ResponseEntity<Integer> getNumberProductOfAllBill();

  ResponseEntity<?> getNumberProductOfBill();

  ResponseEntity<?> getNumberProductOfBillInThreeWeek(Long productId);
}
