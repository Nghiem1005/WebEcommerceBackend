package com.example.webecom.repositories;

import com.example.webecom.entities.Attribute;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
  Optional<Attribute> findAttributeByNameAndValue(String name, String value);
  Optional<Attribute> findAttributeByName(String name);
}
