package com.example.webecom.repositories;

import com.example.webecom.entities.Address;
import com.example.webecom.entities.AddressDetail;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.User;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AddressDetailRepository extends JpaRepository<AddressDetail, Long> {
  @Query("SELECT ad.address from AddressDetail ad where ad.user.id = ?1")
  List<Address> findAddressByUser(Long id);

  @Query("SELECT ad.user from AddressDetail ad where ad.address.id = ?1")
  List<User> findUserByAddress(Long id);

  List<AddressDetail> findAddressDetailByUser(User user);

  List<AddressDetail> findAddressDetailsByUser(User user);
  Optional<AddressDetail> findAddressDetailByUserAndAddress(User user, Address address);

  Optional<AddressDetail> findAddressDetailByUserAndDefaultAddress(User user, boolean defaultAddress);
}
