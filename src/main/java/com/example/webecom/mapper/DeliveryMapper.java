package com.example.webecom.mapper;

import com.example.webecom.dto.request.DeliveryRequestDTO;
import com.example.webecom.dto.response.DeliveryResponseDTO;
import com.example.webecom.entities.Delivery;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
  @Mapping(target = "status", source = "dto.status")
  @Mapping(target = "address", expression = "java(null)")
  @Mapping(target = "bill", expression = "java(null)")
  Delivery deliveryRequestDTOtoDelivery(DeliveryRequestDTO dto);

  @Mapping(target = "id", source = "delivery.id")
  @Mapping(target = "status", source = "delivery.status")
  @Mapping(target = "billId", source = "delivery.bill.id")
  @Mapping(target = "shipperId", source = "delivery.shipper.id")
  @Mapping(target = "shipperPhone", source = "delivery.shipper.phone")
  @Mapping(target = "shipperName", source = "delivery.shipper.name")
  DeliveryResponseDTO deliveryToDeliveryResponseDTO(Delivery delivery);

}