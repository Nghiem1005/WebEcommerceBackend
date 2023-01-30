package com.example.webecom.utils;

import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.models.ItemTotalPage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
  public static final String  DEFAULT_PAGE_SIZE = "10";
  public static final String DEFAULT_PAGE_NUMBER = "1";

  public static double calculateAvgRate(List<Feedback> feedbackList){
    double total = 0;
    if (feedbackList.isEmpty()) return 0;
    for (Feedback feedback : feedbackList){
      total += feedback.getVote();
    }
    return total/feedbackList.size();
  }

  public static ItemTotalPage totalPage(List<?> list, int size){
    int totalPage =  (int) Math.ceil((double) list.size() / size);
    return new ItemTotalPage(list, totalPage);
  }

  public static BigDecimal getTotalPrice(Product product, BigDecimal totalPrice,
      Optional<ProductDiscount> productDiscount, int quantity) {
    if (productDiscount.isPresent()) {
      totalPrice = totalPrice
          .add(product.getStandCost().multiply(
              BigDecimal.valueOf((1 - productDiscount.get().getDiscount().getPercent()) * (double) quantity)))
          .setScale(2, RoundingMode.UP);
    } else {
      totalPrice = totalPrice.add(product.getStandCost().multiply(BigDecimal.valueOf(quantity))).setScale(2,
          RoundingMode.UP);
    }
    return totalPrice;
  }
}
