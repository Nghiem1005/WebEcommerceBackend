package com.example.webecom.repositories;

import com.example.webecom.entities.Role;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findRoleById(Long id);

  Optional<Role> findRoleByName(String name);
}
