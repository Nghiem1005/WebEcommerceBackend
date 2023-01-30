package com.example.webecom.services;

import com.example.webecom.dto.request.UserRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.UserResponseDTO;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {
  AuthResponseDTO findUserByEmailAndPassword(String email, String password);

  ResponseEntity<ResponseObject> saveUser(UserRequestDTO userRequestDTO, String siteUrl)
      throws MessagingException, UnsupportedEncodingException;

  ResponseEntity<ResponseObject> updateUser(Long id, UserRequestDTO userRequestDTO);

  ResponseEntity<?> getAllUser(Pageable pageable);

  ResponseEntity<ResponseObject> deleteUser(Long id);

  ResponseEntity<UserResponseDTO> getUserById(Long id);

  ResponseEntity<ResponseObject> verifyUser(String verifyCode);

  ResponseEntity<Integer> getNumberOfUser();
}
