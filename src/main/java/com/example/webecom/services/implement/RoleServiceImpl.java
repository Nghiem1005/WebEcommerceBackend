package com.example.webecom.services.implement;

import com.example.webecom.dto.request.RoleRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.RoleResponseDTO;
import com.example.webecom.entities.Role;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.RoleMapper;
import com.example.webecom.repositories.RoleRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.RoleService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
  private final RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;
  @Override
  public ResponseEntity<?> getAllRoleOnTrading(Pageable pageable) {
    Page<Role> getRoleList = roleRepository.findAll(pageable);
    List<Role> roleList = getRoleList.getContent();
    List<RoleResponseDTO> roleResponseDTOList = new ArrayList<>();
    for (Role role : roleList) {
      RoleResponseDTO roleResponseDTO = mapper.roleToRoleResponseDTO(role);
      roleResponseDTOList.add(roleResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(roleResponseDTOList);
  }

  @Override
  public ResponseEntity<ResponseObject> createRole(RoleRequestDTO roleRequestDTO) {
    Role role = mapper.roleRequestDTOtoRole(roleRequestDTO);
    role = checkExists(role);

    Role roleSaved = roleRepository.save(role);
    RoleResponseDTO roleResponseDTO = mapper.roleToRoleResponseDTO(roleSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create role successfully!", roleResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateRole(RoleRequestDTO roleRequestDTO, Long id) {
    Role role = mapper.roleRequestDTOtoRole(roleRequestDTO);
    Role getRole = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + id));
    role.setId(id);
    role = checkExists(role);

    Role roleSaved = roleRepository.save(role);
    RoleResponseDTO roleResponseDTO = mapper.roleToRoleResponseDTO(roleSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update role successfully!", roleResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteRole(Long id) {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + id));
    List<User> userList = userRepository.findUsersByRole(role);
    for (User user : userList){
      user.setRole(null);
      userRepository.save(user);
    }
    roleRepository.delete(role);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete role successfully!"));
  }

  @Override
  public ResponseEntity<RoleResponseDTO> getRoleById(Long id) {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + id));
    RoleResponseDTO roleResponseDTO = mapper.roleToRoleResponseDTO(role);

    return ResponseEntity.status(HttpStatus.OK).body(roleResponseDTO);
  }

  private Role checkExists(Role role) {
    // Check role name exists
    Optional<Role> getRole = roleRepository.findRoleByName(role.getName());
    if (getRole.isPresent()) {
      if (role.getId() == null) {
        throw new ResourceAlreadyExistsException("Role name already exists");
      } else {
        if (role.getId() != getRole.get().getId()) {
          throw new ResourceAlreadyExistsException("Role name already exists");
        }
      }
    }
    return role;
  }

}
