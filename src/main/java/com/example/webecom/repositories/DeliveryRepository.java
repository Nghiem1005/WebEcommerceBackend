package com.example.webecom.repositories;

import com.example.webecom.entities.User;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.webecom.entities.Bill;
import com.example.webecom.entities.Delivery;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findDeliveryByBill(Bill bill);
    List<Delivery> findDeliveriesByStatus(String status);

    List<Delivery> findDeliveriesByStatusAndShipper(String status, User shipper);

    @Query(value = "select d from Delivery d where (d.shipper is null or d.shipper.id = :shipperId) and d.address.province = :province and d.status <>  'cancel' and d.status <> 'waiting'")
    List<Delivery> getDeliveryByProvinceAndShipper(@Param("province") String province, @Param("shipperId") Long shipperId);

    @Query(value = "select d from Delivery d where d.status = 'checked' and d.address.province = :province")
    List<Delivery> findDeliveryByCheckedAndAddress(@Param("province") String province);
}
