package com.example.webecom.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.webecom.dto.request.RoleRequestDTO;
import com.example.webecom.dto.response.RoleResponseDTO;
import com.example.webecom.entities.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "description", source = "dto.description")
  Role roleRequestDTOtoRole(RoleRequestDTO dto);

  @Mapping(target = "id", source = "role.id")
  @Mapping(target = "name", source = "role.name")
  @Mapping(target = "description", source = "role.description")
  RoleResponseDTO roleToRoleResponseDTO(Role role);

}