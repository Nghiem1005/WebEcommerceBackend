package com.example.webecom.controller;

import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.utils.JwtTokenUtil;
import com.example.webecom.dto.request.AuthRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.User;
import com.example.webecom.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  @Autowired UserService userService;

  @Autowired AuthenticationManager auth;

  @Autowired JwtTokenUtil jwtUtil;

  public static final String ADMIN_URL = "http://localhost:3000";

  @PostMapping (value = "/auth/login")
  public ResponseEntity<ResponseObject> getUserByEmailAndPassword(@RequestBody @Valid AuthRequestDTO authRequestDTO) {
    UsernamePasswordAuthenticationToken authenticationToken;
    if (authRequestDTO.getEmail() == null){
      authenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getPhone(), authRequestDTO.getPassword());
    } else {
      authenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword());
    }
    Authentication authentication = auth.authenticate(authenticationToken);
    User user = (User) authentication.getPrincipal();
    //String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    if (authRequestDTO.getRole().equals("Admin")){
      if (user.getRole().getId() != 1){
        throw new ResourceNotFoundException("Account has no permissions");
      }
    } else {
      if (user.getRole().getId() == 2 && authRequestDTO.getRole().equals("Shipper")){
        throw new ResourceNotFoundException("Account has no permissions");
      }
    }
    String accessToken = jwtUtil.generateToken(user);
    AuthResponseDTO authResponseDTO = new AuthResponseDTO(user.getId(), user.getName(), accessToken);
    return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, "Login Successfully", authResponseDTO));
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<ResponseObject> verifyUser(@RequestParam(name = "code") String verifyCode){
    return userService.verifyUser(verifyCode);
  }
}
