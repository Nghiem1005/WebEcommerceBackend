package com.example.webecom.services.implement;

import com.example.webecom.dto.request.AddressRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

  @Override
  public ResponseEntity<ResponseObject> createAddress(AddressRequestDTO addressRequestDTO) {

    return null;
  }
}
