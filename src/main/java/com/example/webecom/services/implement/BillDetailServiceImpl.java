package com.example.webecom.services.implement;

import com.example.webecom.dto.response.BillDetailResponseDTO;
import com.example.webecom.dto.response.BillResponseDTO;
import com.example.webecom.dto.response.CartDetailResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Bill;
import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.Cart;
import com.example.webecom.entities.CartDetail;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.exceptions.InvalidValueException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.BillDetailMapper;
import com.example.webecom.mapper.UserMapper;
import com.example.webecom.models.Item;
import com.example.webecom.repositories.BillDetailRepository;
import com.example.webecom.repositories.BillRepository;
import com.example.webecom.repositories.ProductDiscountRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.services.BillDetailService;
import com.example.webecom.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillDetailServiceImpl implements BillDetailService {
  @Autowired private BillRepository billRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private BillDetailRepository billDetailRepository;
  @Autowired private ProductDiscountRepository productDiscountRepository;
  private final BillDetailMapper billDetailMapper = Mappers.getMapper(BillDetailMapper.class);

  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  @Override
  public ResponseEntity<ResponseObject> addProductToBill(Long billId, Long productId, int amount) {
    BillDetail billDetail = new BillDetail();
    Bill bill = billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    BigDecimal totalPrice = bill.getTotalPrice();
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    Optional<BillDetail> getBillDetail = billDetailRepository.findBillDetailByBillAndProduct(bill, product);
    if (amount > product.getAmount()){
      throw new InvalidValueException("Amount product " + product.getName() + " must be less than amount product exists");
    } else if(amount < 0) {
      throw new InvalidValueException("Amount product must greater than 1");
    } else {
      //Update amount product
      int newAmountProduct = product.getAmount() - amount;
      product.setAmount(newAmountProduct);
      productRepository.save(product);
      if (getBillDetail.isPresent()){
        int newAmount = getBillDetail.get().getQuantity() + amount;
        billDetail = getBillDetail.get();
        billDetail.setQuantity(newAmount);
      } else {
        billDetail.setBill(bill);
        billDetail.setProduct(product);
        billDetail.setQuantity(amount);

      }

      //Total price of bill
      totalPrice = totalPrice.add(totalPrice(product, bill, amount));
      bill.setTotalPrice(totalPrice);
      billRepository.save(bill);
    }
    BillDetailResponseDTO billDetailResponseDTO = billDetailMapper.billDetailToBillDetailResponseDTO(billDetailRepository.save(billDetail));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add product to bill success!", billDetailResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteProductToBill(Long billId, Long productId, int amount) {
    Bill bill = billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    BigDecimal totalPrice = bill.getTotalPrice();
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    BillDetail billDetail = billDetailRepository.findBillDetailByBillAndProduct(bill, product).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId + " and bill ID = " + billId));
    if (amount > billDetail.getQuantity()){
      throw new InvalidValueException("Amount product " + product.getName() + " must be less than amount product exists in bill");
    } else if(amount < 0) {
      throw new InvalidValueException("Amount product must greater than 1");
    }else if (billDetail.getQuantity() == amount){
      billDetailRepository.delete(billDetail);
    } else {
      //Update amount product
      int newAmountProduct = billDetail.getQuantity() - amount;
      billDetail.setQuantity(newAmountProduct);
    }
    Optional<ProductDiscount> productDiscount = productDiscountRepository.findAllDiscountByProductIdAndDate(product.getId(), bill.getCreateDate());
    //totalPrice = totalPrice.subtract(totalPrice(product, bill, amount));
    totalPrice = totalPrice.subtract(Utils.getTotalPrice(product, BigDecimal.valueOf(0.00), productDiscount, amount));
    bill.setTotalPrice(totalPrice);
    billRepository.save(bill);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete product in bill success!"));
  }

  private BigDecimal totalPrice(Product product, Bill bill, int amount){
    BigDecimal totalPrice;
    Optional<ProductDiscount> productDiscount = productDiscountRepository.findAllDiscountByProductIdAndDate(product.getId(), bill.getCreateDate());
    if (productDiscount.isPresent()){
      totalPrice = product.getStandCost().multiply(
          BigDecimal.valueOf((productDiscount.get().getDiscount().getPercent()) * (double)amount)).setScale(2, RoundingMode.UP);
    } else {
      totalPrice = product.getStandCost().multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.UP);
    }
    return totalPrice;
  }

  @Override
  public ResponseEntity<?> getProductByBill(Long billId) {
    Bill bill = billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    List<BillDetail> billDetailList = billDetailRepository.findBillDetailByBill(bill);
    List<BillDetailResponseDTO> billDetailResponseDTOList = new ArrayList<>();
    for (BillDetail billDetail : billDetailList){
      BillDetailResponseDTO billDetailResponseDTO = billDetailMapper.billDetailToBillDetailResponseDTO(billDetail);
      billDetailResponseDTOList.add(billDetailResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(billDetailResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getAllProductToBillPayed() {
    Sort sort = Sort.by("createDate").descending();
    List<Bill> billList = billRepository.findAll(sort);
    List<BillDetailResponseDTO> billDetailResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      List<BillDetail> billDetailList = billDetailRepository.findBillDetailByBill(bill);
      for (BillDetail billDetail : billDetailList){
        BillDetailResponseDTO billDetailResponseDTO = billDetailMapper.billDetailToBillDetailResponseDTO(billDetail);
        Optional<ProductDiscount> productDiscount = productDiscountRepository.findAllDiscountByProductIdAndDate(billDetail.getProduct()
            .getId(), billDetail.getBill().getCreateDate());
        billDetailResponseDTO.setPrice(Utils.getTotalPrice(billDetail.getProduct(), BigDecimal.valueOf(0.00), productDiscount, billDetail.getQuantity()));
        billDetailResponseDTO.setUserResponseDTO(userMapper.userToUserResponseDTO(bill.getUser()));
        billDetailResponseDTOList.add(billDetailResponseDTO);
      }
    }

    return ResponseEntity.status(HttpStatus.OK).body(billDetailResponseDTOList);
  }
}
