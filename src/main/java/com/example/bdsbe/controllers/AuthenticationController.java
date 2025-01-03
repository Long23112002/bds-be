package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.request.LoginRequest;
import com.example.bdsbe.dtos.request.UserRequest;
import com.example.bdsbe.dtos.response.LoginResponse;
import com.example.bdsbe.dtos.response.UserResponse;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.services.users.AuthenticationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  @Autowired private AuthenticationService authenticationService;

  @PostMapping("/register")
  public User register(@Valid @RequestBody UserRequest request) {
    return authenticationService.register(request);
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return authenticationService.login(request);
  }

  @GetMapping("/user_info")
  public UserResponse getUserInfo(@RequestHeader("Authorization") String token) {
    return authenticationService.getUserInfo(token);
  }
}
