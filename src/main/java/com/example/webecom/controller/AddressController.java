package com.example.webecom.controller;

import com.example.webecom.dto.request.AddressRequestDTO;
import com.example.webecom.dto.response.AddressDetailResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.AddressDetailService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
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
@RequestMapping(value = "/api/v1/address")
public class AddressController {
  @Autowired private AddressDetailService addressDetailService;
  
  @PostMapping(value = "/user")
  public ResponseEntity<ResponseObject> createAddressUser(@RequestBody AddressRequestDTO addressRequestDTO, @RequestParam(name = "userId") Long userId){
    return addressDetailService.createAddressDetail(addressRequestDTO, userId);
  }

  @GetMapping(value = "/user")
  public ResponseEntity<?> getAddressByUser(@RequestParam(name = "userId") Long userId){
    return addressDetailService.getAddressByUser(userId);
  }
  @PutMapping(value = "/user")
  public ResponseEntity<ResponseObject> updateAddressUser(@RequestBody AddressRequestDTO addressRequestDTO,
      @RequestParam(name = "userId") Long userId,
      @RequestParam(name = "addressId") Long addressId){
    return addressDetailService.updateAddressDetail(addressId, userId, addressRequestDTO);
  }

  @DeleteMapping(value = "/user")
  public ResponseEntity<ResponseObject> deleteAddressUser(@RequestParam(name = "userId") Long userId,
      @RequestParam(name = "addressId") Long addressId){
    return addressDetailService.deleteAddressDetail(addressId, userId);
  }
}
