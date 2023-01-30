package com.example.webecom.mapper;

import com.example.webecom.dto.response.AddressDetailResponseDTO;
import com.example.webecom.entities.AddressDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressDetailMapper {
  @Mapping(target = "user", source = "add.user.id")
  @Mapping(target = "address", source = "add.address")
  @Mapping(target = "defaultAddress", source = "add.defaultAddress")
  AddressDetailResponseDTO addressDetailToAddressDetailResponseDTO(AddressDetail add);
}
