package com.example.webecom.services;

import com.example.webecom.dto.request.RoleRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.RoleResponseDTO;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<?> getAllRoleOnTrading(Pageable pageable);

    ResponseEntity<ResponseObject> createRole(RoleRequestDTO roleRequestDTO);

    ResponseEntity<ResponseObject> updateRole(RoleRequestDTO roleRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteRole(Long id);

    ResponseEntity<RoleResponseDTO> getRoleById(Long id);

}