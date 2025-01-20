package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.request.LoginRequest;
import com.example.bdsbe.dtos.request.UserRequest;
import com.example.bdsbe.dtos.response.AuthResponse;
import com.example.bdsbe.dtos.response.LoginResponse;
import com.example.bdsbe.dtos.response.UserProfileResponse;
import com.example.bdsbe.dtos.response.UserResponse;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.services.posts.PostService;
import com.example.bdsbe.services.users.AuthenticationService;
import com.example.bdsbe.services.users.RefreshTokenService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  @Autowired private AuthenticationService authenticationService;
  @Autowired private RefreshTokenService refreshTokenService;
  @Autowired private PostService postService;

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

  @GetMapping("/error")
  public void testErrro() throws Exception {
    throw new Exception("Error");
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<AuthResponse> refreshToken(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    AuthResponse res = refreshTokenService.refreshToken(request, response);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/profile_info/{id}")
  public UserProfileResponse getProfileById(
      @PathVariable("id") Long id,
      Pageable pageable,
      @RequestParam(defaultValue = "SELL") Demand demand) {
    return authenticationService.getUserProfile(id, pageable, demand);
  }
}
