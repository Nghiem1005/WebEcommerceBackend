package com.example.webecom.services;

import com.example.webecom.dto.request.AttributeRequestDTO;
import com.example.webecom.dto.response.AttributeProductResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.AttributeProduct;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AttributeProductService {
  ResponseEntity<ResponseObject> createAttributeProduct(Long productId, AttributeRequestDTO attributeRequestDTO);
  ResponseEntity<ResponseObject> updateAttributeProduct(Long productId, Long attributeId, AttributeRequestDTO attributeRequestDTO);
  ResponseEntity<?> getAttributeProductByProduct(Long productId);

}
