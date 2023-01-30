package com.example.webecom.controller;

import com.example.webecom.dto.request.UserRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.UserResponseDTO;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.UserService;
import com.example.webecom.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private ImageStorageService imageStorageService;

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createUser(@ModelAttribute @Valid UserRequestDTO userRequestDTO,
      HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    return userService.saveUser(userRequestDTO, siteUrl);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> updateUser(@PathVariable(name = "id") Long id,
      @ModelAttribute @Valid UserRequestDTO userRequestDTO) {
    return userService.updateUser(id, userRequestDTO);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllUser(
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return userService.getAllUser(pageable);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> deleteUser(@PathVariable(name = "id") Long id) {
    return userService.deleteUser(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(name = "id") Long id) {
    return userService.getUserById(id);
  }
}
