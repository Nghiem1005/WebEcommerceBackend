package com.example.webecom.services;

import com.example.webecom.dto.request.AddressRequestDTO;
import com.example.webecom.dto.response.AddressDetailResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.AddressDetail;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AddressDetailService {
  ResponseEntity<ResponseObject> createAddressDetail(AddressRequestDTO addressRequestDTO, Long userId);
  ResponseEntity<?> getAddressByUser(Long id);
  ResponseEntity<ResponseObject> updateAddressDetail(Long addressId, Long userId, AddressRequestDTO addressRequestDTO);
  ResponseEntity<ResponseObject> deleteAddressDetail(Long addressId, Long userId);
}
