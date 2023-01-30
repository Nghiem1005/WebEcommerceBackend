package com.example.webecom.mapper;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.response.BillResponseDTO;
import com.example.webecom.entities.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillMapper {
  @Mapping(target = "userName", source = "b.user.name")
  @Mapping(target = "billId", source = "b.id")
  @Mapping(target = "status", source = "b.status")
  @Mapping(target = "paymentMethod", source = "b.paymentMethod")
  @Mapping(target = "totalPrice", source = "totalPrice")
  @Mapping(target = "payDate", source = "b.payDate")
  @Mapping(target = "userId", source = "b.user.id")
  BillResponseDTO billToBillResponseDTO(Bill b);

  @Mapping(target = "status", source = "b.status")
  @Mapping(target = "payDate", source = "b.payDate")
  @Mapping(target = "paymentMethod", source = "b.paymentMethod")
  Bill billRequestDTOtoBill(BillRequestDTO b);
}
