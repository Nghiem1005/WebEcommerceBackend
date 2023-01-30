package com.example.webecom.controller;

import com.example.webecom.dto.request.AddressRequestDTO;
import com.example.webecom.dto.request.DiscountRequestDTO;
import com.example.webecom.dto.response.DiscountResponseDTO;
import com.example.webecom.dto.response.ProductDiscountResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.ProductDiscountService;
import com.example.webecom.utils.Utils;
import com.example.webecom.services.DiscountService;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/discount")
public class DiscountController {
  @Autowired
  private ProductDiscountService productDiscountService;
  @Autowired
  private DiscountService discountService;

  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> createProductDiscount(@RequestParam(name = "productId") Long productId,
      @RequestParam(name = "discountId") Long discountId) {
    return productDiscountService.createProductDiscount(productId, discountId);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getProductDiscount(@RequestParam(name = "productId") Long productId) {
    return productDiscountService.getProductDiscount(productId);
  }

  @GetMapping(value = "/product/current")
  public ResponseEntity<?> getProductDiscountCurrent(@RequestParam(name = "productId") Long productId) {
    return productDiscountService.getProductDiscountCurrentByProduct(productId);
  }

  @DeleteMapping(value = "/product")
  public ResponseEntity<ResponseObject> deleteProductDiscount(@RequestParam(name = "productId") Long productId,
      @RequestParam(name = "discountId") Long discountId) {
    return productDiscountService.deleteProductDiscount(productId, discountId);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllDiscountOnTrading(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return discountService.getAllDiscountOnTrading(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createDiscount(@ModelAttribute @Valid DiscountRequestDTO discountRequestDTO) {
    return discountService.createDiscount(discountRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateDiscount(@ModelAttribute @Valid DiscountRequestDTO discountRequestDTO,
      @RequestParam(name = "id") Long id) {
    return discountService.updateDiscount(discountRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteDiscount(@RequestParam(name = "id") Long id) {
    return discountService.deleteDiscount(id);
  }

  @GetMapping(value = "/{id}")
  public DiscountResponseDTO getDiscountById(@PathVariable(name = "id") Long id) {
    return discountService.getDiscountById(id);
  }
}
