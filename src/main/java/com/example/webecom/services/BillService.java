package com.example.webecom.services;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.response.ResponseObject;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface BillService {
  ResponseEntity<?> getBillByUser(Long userId);

  ResponseEntity<ResponseObject> createBill(Long userId, BillRequestDTO billRequestDTO, Long addressId);

  ResponseEntity<ResponseObject> updateBill(Long billId, BillRequestDTO billRequestDTO);

  ResponseEntity<?> getAllBill();

  ResponseEntity<?> getBillByStatus(String status);

  ResponseEntity<ResponseObject> deleteBill(Long billId);

  ResponseEntity<?> getBillById(Long id);

  ResponseEntity<Integer> getNumberOfBill();

  ResponseEntity<Double> getSales();
}
