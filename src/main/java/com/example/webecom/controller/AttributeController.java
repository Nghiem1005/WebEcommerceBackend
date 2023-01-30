package com.example.webecom.controller;

import com.example.webecom.dto.request.AddressRequestDTO;
import com.example.webecom.dto.request.AttributeRequestDTO;
import com.example.webecom.dto.response.AddressDetailResponseDTO;
import com.example.webecom.dto.response.AttributeProductResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.AttributeProductService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/attribute")
public class AttributeController {
  @Autowired
  private AttributeProductService attributeProductService;

  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> createAttributeProduct(@ModelAttribute @Valid AttributeRequestDTO attributeRequestDTO, @RequestParam(name = "productId") Long productId){
    return attributeProductService.createAttributeProduct(productId, attributeRequestDTO);
  }

  @PutMapping(value = "/product")
  public ResponseEntity<ResponseObject> updateAttributeProduct(@ModelAttribute @Valid AttributeRequestDTO attributeRequestDTO,
      @RequestParam(name = "productId") Long productId,
      @RequestParam(name = "attributeId") Long attributeId){
    return attributeProductService.updateAttributeProduct(productId, attributeId, attributeRequestDTO);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getAttributeByProduct(@RequestParam(name = "productId") Long productId){
    return attributeProductService.getAttributeProductByProduct(productId);
  }
}
