package com.example.bdsbe.services.users;

import com.example.bdsbe.dtos.request.LoginRequest;
import com.example.bdsbe.dtos.request.UserRequest;
import com.example.bdsbe.dtos.response.AuthResponse;
import com.example.bdsbe.dtos.response.JwtResponse;
import com.example.bdsbe.dtos.response.LoginResponse;
import com.example.bdsbe.dtos.response.UserResponse;
import com.example.bdsbe.entities.users.Role;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.exceptions.ErrorMessage;
import com.example.bdsbe.repositories.users.RoleRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  @Autowired private JwtService jwtService;

  @Autowired private RefreshTokenService refreshTokenService;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private UserRepository userRepository;

  @Autowired private RedisTokenService redisTokenService;

  @Autowired private RoleRepository roleRepository;

  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication;

    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getPhoneNumber(), loginRequest.getPassword()));
    } catch (AuthenticationException e) {
      throw new ExceptionHandle(HttpStatus.UNAUTHORIZED, ErrorMessage.BAD_CREDENTIAL.val());
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();

    // Lấy các token cũ từ Redis
    String existingAccessToken =
        redisTokenService.getAccessTokenByUserId(String.valueOf(user.getId()));
    String existingRefreshToken =
        redisTokenService.getRefreshTokenByUserId(String.valueOf(user.getId()));

    // Nếu tồn tại token cũ, đưa vào blacklist
    if (existingAccessToken != null) {
      long remainingTime = jwtService.getRemainingExpirationTime(existingAccessToken);
      redisTokenService.blacklistToken(existingAccessToken, remainingTime);
      redisTokenService.deleteAccessToken(existingAccessToken);
    }
    //    if (existingRefreshToken != null) {
    //      long remainingTime = jwtService.getRemainingExpirationTime(existingRefreshToken);
    //      redisTokenService.blacklistToken(existingRefreshToken, remainingTime);
    //      redisTokenService.deleteRefreshToken(existingRefreshToken);
    //    }

    // Tạo token mới
    String accessToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    long accessTokenExpiration = jwtService.getAccessTokenExpiration();
    long refreshTokenExpiration = jwtService.getRefreshTokenExpiration();

    // Lưu token mới vào Redis
    redisTokenService.saveAccessToken(
        accessToken, String.valueOf(user.getId()), accessTokenExpiration);
    redisTokenService.saveRefreshToken(
        refreshToken, String.valueOf(user.getId()), refreshTokenExpiration);

    refreshTokenService.revokeAllUserToken(user);
    refreshTokenService.saveUserToken(user, refreshToken);

    // Build và trả về LoginResponse
    return LoginResponse.builder()
        .authResponse(
            AuthResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build())
        .userResponse(
            UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .wallet(user.getWallet())
                .isAdmin(user.getIsAdmin())
                .build())
        .build();
  }

  public User register(UserRequest userRequest) {
    try {
      User user = new User();
      FnCommon.copyProperties(user, userRequest);
      user.setCode("USER" + userRepository.seq());
      user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
      List<Role> roles = new ArrayList<>();
      Role userRole = roleRepository.findByCode("USER");
      roles.add(userRole);
      user.setRoles(roles);
      return userRepository.save(user);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ExceptionHandle(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  public UserResponse getUserInfo(String token) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    User user =
        userRepository
            .findById(jwtResponse.getUserId())
            .orElseThrow(
                () -> new ExceptionHandle(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.val()));
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .phoneNumber(user.getPhoneNumber())
        .avatar(user.getAvatar())
        .wallet(user.getWallet())
        .isAdmin(user.getIsAdmin())
        .build();
  }
}
