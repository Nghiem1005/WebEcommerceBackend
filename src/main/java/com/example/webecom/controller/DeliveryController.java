package com.example.webecom.controller;

import com.example.webecom.dto.request.DeliveryRequestDTO;
import com.example.webecom.dto.response.DeliveryResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.DeliveryService;
import com.example.webecom.utils.Utils;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/delivery")
public class DeliveryController {
  @Autowired
  private DeliveryService deliveryService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllDeliveryOnTrading(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return deliveryService.getAllDeliveryOnTrading(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createDelivery(@ModelAttribute @Valid DeliveryRequestDTO deliveryRequestDTO) {
    return deliveryService.createDelivery(deliveryRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateDelivery(@RequestBody @Valid DeliveryRequestDTO deliveryRequestDTO,
      @RequestParam(name = "id") Long id) {
    return deliveryService.updateDelivery(deliveryRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteDelivery(@RequestParam(name = "id") Long id) {
    return deliveryService.deleteDelivery(id);
  }

  @GetMapping(value = "/{id}")
  public DeliveryResponseDTO getDeliveryById(@PathVariable(name = "id") Long id) {
    return deliveryService.getDeliveryById(id);
  }

  @GetMapping(value = "/status")
  public ResponseEntity<?> getDeliveryByStatus(@RequestParam(name = "status") String status) {
    return deliveryService.getDeliveryByStatus(status);
  }

  @GetMapping(value = "/shipper")
  public ResponseEntity<?> getDeliveryByStatus(@RequestParam(name = "shipperId") Long shipperId) {
    return deliveryService.getDeliveryCheckedAndAddress(shipperId);
  }

  @GetMapping(value = "/shipper/status")
  public ResponseEntity<?> getDeliveryByStatusAndShipper(@RequestParam(name = "shipperId") Long shipperId, @RequestParam(name = "status") String status) {
    return deliveryService.getDeliveryByStatusAndShipper(shipperId, status);
  }

  @GetMapping(value = "/shipper/province")
  public ResponseEntity<?> getDeliveryByProvinceAndShipper(@RequestParam(name = "shipperId") Long shipperId) {
    return deliveryService.getDeliveryByProvinceAndShipper(shipperId);
  }
}
