package com.example.webecom.repositories;

import com.example.webecom.entities.Bill;
import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.Product;
import com.example.webecom.models.IProductQuantity;
import com.example.webecom.models.IStatisticDay;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
  List<BillDetail> findBillDetailByBill(Bill bill);

  List<BillDetail> findBillDetailByProduct(Product product);
  Optional<BillDetail> findBillDetailByBillAndProduct(Bill bill, Product product);

  @Query(value = "select sum (b.quantity) from BillDetail b where b.bill.status = 'paid'")
  Optional<Integer> numberProductOfAllBill();

  @Query(value = "select b.product as product, sum (b.quantity) as quantity from BillDetail b where b.bill.status = 'paid' group by b.product")
  List<IProductQuantity> numberProductOfBill(Sort sort);

  @Query(value = "select sum(quantity) from tbl_bill_detail inner join tbl_bill on tbl_bill_detail.bill_id = tbl_bill.id\n"
      + "      where tbl_bill_detail.product_id = :productId and tbl_bill.pay_date <= date_sub(current_date(), interval :startDay Day)  and tbl_bill.pay_date > date_sub(current_date(), interval :endDay Day)", nativeQuery = true)
  Optional<Integer> numberProductInWeek(@Param("startDay") int startDay, @Param("endDay") int endDay, @Param("productId") Long productId);
}
