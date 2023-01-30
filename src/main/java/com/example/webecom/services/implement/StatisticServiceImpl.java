package com.example.webecom.services.implement;

import com.example.webecom.dto.response.ProductResponseDTO;
import com.example.webecom.dto.response.ProductStatisticResponseDTO;
import com.example.webecom.dto.response.StatisticResponseDTO;
import com.example.webecom.entities.Bill;
import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.ProductMapper;
import com.example.webecom.models.IProductQuantity;
import com.example.webecom.models.IStatisticDay;
import com.example.webecom.repositories.BillDetailRepository;
import com.example.webecom.repositories.BillRepository;
import com.example.webecom.repositories.ProductDiscountRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.StatisticService;
import com.example.webecom.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {
  @Autowired
  private UserRepository userRepository;
  @Autowired private BillRepository billRepository;

  @Autowired private BillDetailRepository billDetailRepository;

  @Autowired private ProductDiscountRepository productDiscountRepository;

  private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
  @Override
  public ResponseEntity<?> getAllUserByDay() {
    List<IStatisticDay> userList = userRepository.findAllUserByDay();
    return statisticByDay(7, userList);
  }

  @Override
  public ResponseEntity<?> getRevenueByDay() {
    List<IStatisticDay> userList = billRepository.findRevenueByDay();
    return statisticByDay(5, userList);
  }

  @Override
  public ResponseEntity<Integer> getNumberProductOfAllBill() {
    int numberProduct = billDetailRepository.numberProductOfAllBill().orElseThrow(() -> new ResourceNotFoundException("Not found product on bill"));
    return ResponseEntity.status(HttpStatus.OK).body(numberProduct);
  }

  @Override
  public ResponseEntity<?> getNumberProductOfBill() {
    Sort sort = Sort.by("quantity").descending();
    List<IProductQuantity> numberProduct = billDetailRepository.numberProductOfBill(sort);
    List<ProductStatisticResponseDTO> productResponseDTOList = new ArrayList<>();
    for (IProductQuantity iProductQuantity : numberProduct){
      Product product = iProductQuantity.getProduct();
      ProductStatisticResponseDTO productResponseDTO = productMapper.productToProductStatisticResponseDTO(iProductQuantity);
      BigDecimal totalPrice = (BigDecimal.valueOf(0.00));
      List<BillDetail> billDetailList = billDetailRepository.findBillDetailByProduct(product);
      for (BillDetail billDetail : billDetailList){
        // Total price product all bill
        Optional<ProductDiscount> productDiscount = productDiscountRepository.findAllDiscountByProductIdAndDate(iProductQuantity.getProduct().getId(),
                billDetail.getBill().getCreateDate());
        totalPrice = Utils.getTotalPrice(product, totalPrice, productDiscount, billDetail.getQuantity());
      }
      productResponseDTO.setTotalPrice(totalPrice);
      productResponseDTOList.add(productResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getNumberProductOfBillInThreeWeek(Long productId) {
    List<StatisticResponseDTO> statisticResponseDTOList = new ArrayList<>();

    for (int i=0; i<3; i++){
      StatisticResponseDTO statisticResponseDTO = new StatisticResponseDTO();
      statisticResponseDTO.setTimes("Week " + (i + 1));
      statisticResponseDTO.setValue(0);
      Optional<Integer> value = billDetailRepository.numberProductInWeek(i*7, (i+1)*7, productId);
      if (value.isPresent()){
        statisticResponseDTO.setValue(value.get());
      }
      statisticResponseDTOList.add(statisticResponseDTO);
    }

    return ResponseEntity.status(HttpStatus.OK).body(statisticResponseDTOList);
  }

  public ResponseEntity<?> statisticByDay(int amountDay, List<IStatisticDay> userList){
    String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    List<StatisticResponseDTO> statisticResponseDTOList = new ArrayList<>();



    List<String> dayList = new ArrayList<>();
    for (int i=0; i < amountDay; i++){
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_WEEK, - i);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek == 1){
        dayList.add("Sun");
      } else {
        dayList.add(daysOfWeek[dayOfWeek - 2]);
      }
    }

    for (String day : dayList){
      StatisticResponseDTO statisticResponseDTO = new StatisticResponseDTO();
      statisticResponseDTO.setValue(0);
      statisticResponseDTO.setTimes(day);
      for (IStatisticDay user : userList) {
        if (daysOfWeek[user.getWeekDay()].equals(day)){
          statisticResponseDTO.setValue(user.getTotalValue());
        }
      }
      statisticResponseDTOList.add(statisticResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(statisticResponseDTOList);
  }
}
