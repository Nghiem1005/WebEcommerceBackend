package com.example.webecom.repositories;

import com.example.webecom.entities.Bill;
import com.example.webecom.entities.User;
import com.example.webecom.models.IStatisticDay;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
  List<Bill> findBillByUser(User user);

  @Query(value = "select count(b) from Bill b")
  Optional<Integer> getNumberOfBill();

  @Query(value = "select sum(b.totalPrice) as sales from Bill b where b.status = 'paid'")
  Optional<Double> getSales();

  List<Bill> findBillsByStatus(String status);

  List<Bill> findAll(Sort sort);

  @Query(value = "select weekday(create_date) as weekDay, SUM(total_price) as totalValue  from tbl_bill \n"
      + "      where status = 'paid' and create_date < now() and create_date > date_sub(current_date(), interval 5 Day) \n"
      + "      group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findRevenueByDay();
}
