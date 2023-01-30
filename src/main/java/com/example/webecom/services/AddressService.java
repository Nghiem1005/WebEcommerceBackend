package com.example.webecom.services;

import com.example.webecom.dto.request.AddressRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface AddressService {
  ResponseEntity<ResponseObject> createAddress(AddressRequestDTO addressRequestDTO);
}
