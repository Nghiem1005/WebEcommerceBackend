package com.example.webecom.repositories;

import com.example.webecom.entities.Role;
import com.example.webecom.entities.User;
import com.example.webecom.models.IStatisticDay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByPhone(String phone);

  Optional<User> findUserByVerificationCode(String verifyCode);
  List<User> findUsersByRole(Role role);

  @Query(value = "select count (u) from User u where u.role.id= 2")
  Optional<Integer> getNumberOfUser();

  @Query(value = "select weekday(create_date) as weekDay, count(id) as totalValue  from tbl_user "
      + "where create_date <= current_date() and create_date > date_sub(current_date(), interval 7 Day) "
      + "group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findAllUserByDay();
}
