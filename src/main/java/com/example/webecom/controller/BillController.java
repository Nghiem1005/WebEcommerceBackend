package com.example.webecom.controller;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.BillDetailService;
import com.example.webecom.services.BillService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/bill")
public class BillController {
  @Autowired
  private BillService billService;
  @Autowired
  private BillDetailService billDetailService;

  @GetMapping(value = "/user")
  public ResponseEntity<?> getBillByUser(@RequestParam(name = "userId") Long userId) {
    return billService.getBillByUser(userId);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getBillById(@PathVariable(name = "id") Long id) {
    return billService.getBillById(id);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createBill(@RequestParam(name = "userId") Long userId,
      @RequestBody BillRequestDTO billRequestDTO, @RequestParam(name = "addressID") Long addressId) {
    return billService.createBill(userId, billRequestDTO, addressId);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateBill(@RequestParam(name = "billId") Long billId,
      @RequestBody BillRequestDTO billRequestDTO) {
    return billService.updateBill(billId, billRequestDTO);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllBill() {
    return billService.getAllBill();
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteBill(@RequestParam(name = "billId") Long billId) {
    return billService.deleteBill(billId);
  }

  @DeleteMapping(value = "/product/{amount}")
  public ResponseEntity<ResponseObject> deleteBillProduct(@RequestParam(name = "billId") Long billId,
      @RequestParam(name = "productId") Long productId, @PathVariable(name = "amount") int amount) {
    return billDetailService.deleteProductToBill(billId, productId, amount);
  }

  @PostMapping(value = "/product/{amount}")
  public ResponseEntity<ResponseObject> addProductToBill(@RequestParam(name = "billId") Long billId,
      @RequestParam(name = "productId") Long productId, @PathVariable(name = "amount") int amount) {
    return billDetailService.addProductToBill(billId, productId, amount);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getBillProduct(@RequestParam(name = "billId") Long billId) {
    return billDetailService.getProductByBill(billId);
  }

  @GetMapping(value = "/product/payed")
  public ResponseEntity<?> getAllProductToBillPayed() {
    return billDetailService.getAllProductToBillPayed();
  }

  @GetMapping(value = "/status")
  public ResponseEntity<?> getBillByStatus(@RequestParam(name = "status") String status) {
    return billService.getBillByStatus(status);
  }
}
