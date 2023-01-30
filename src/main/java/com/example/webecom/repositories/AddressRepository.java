package com.example.webecom.repositories;

import com.example.webecom.entities.Address;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {
  Optional<Address> findAddressByProvinceAndDistrictAndWardAndApartmentNumber(String province, String district, String ward, String apartmentNumber);
}
