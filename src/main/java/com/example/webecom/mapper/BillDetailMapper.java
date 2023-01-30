package com.example.webecom.mapper;

import com.example.webecom.dto.response.BillDetailResponseDTO;
import com.example.webecom.dto.response.CartDetailResponseDTO;
import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.CartDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillDetailMapper {
  @Mapping(target = "productId", source = "b.product.id")
  @Mapping(target = "billId", source = "b.bill.id")
  @Mapping(target = "amount", source = "b.quantity")
  @Mapping(target = "productName", source = "b.product.name")
  @Mapping(target = "productThumbnail", source = "b.product.thumbnail")
  @Mapping(target = "payDate", source = "b.bill.payDate")
  @Mapping(target = "payMethod", source = "b.bill.paymentMethod")
  @Mapping(target = "status", source = "b.bill.status")
  @Mapping(target = "price", expression = "java(null)")
  BillDetailResponseDTO billDetailToBillDetailResponseDTO(BillDetail b);
}
