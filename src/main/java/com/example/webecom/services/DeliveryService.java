package com.example.webecom.services;

import com.example.webecom.dto.request.DeliveryRequestDTO;
import com.example.webecom.dto.response.DeliveryResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DeliveryService {
    ResponseEntity<?> getAllDeliveryOnTrading(Pageable pageable);

    ResponseEntity<ResponseObject> createDelivery(DeliveryRequestDTO deliveryRequestDTO);

    ResponseEntity<ResponseObject> updateDelivery(DeliveryRequestDTO deliveryRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteDelivery(Long id);

    DeliveryResponseDTO getDeliveryById(Long id);

    ResponseEntity<?> getDeliveryByStatus(String status);

    ResponseEntity<?> getDeliveryByStatusAndShipper(Long shipperId, String status);

    ResponseEntity<?> getDeliveryByProvinceAndShipper(Long shipperId);

    ResponseEntity<?> getDeliveryCheckedAndAddress(Long shipperId);

    DeliveryResponseDTO getDeliveryByBill(Long billId);
}