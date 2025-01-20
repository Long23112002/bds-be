package com.example.bdsbe.services.users;

import com.example.bdsbe.dtos.request.LoginRequest;
import com.example.bdsbe.dtos.request.UserRequest;
import com.example.bdsbe.dtos.response.*;
import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.entities.users.Role;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.exceptions.ErrorMessage;
import com.example.bdsbe.repositories.posts.PostRepository;
import com.example.bdsbe.repositories.users.RoleRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.example.bdsbe.utils.ResponsePage;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  @Autowired private PostRepository postRepository;

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
    User user = getUserById(jwtResponse.getUserId());
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

  public UserProfileResponse getUserProfile(Long id, Pageable pageable, Demand demand) {
    User user = getUserById(id);
    Page<Post> posts = postRepository.findByUserIdAndDemand(id, demand, pageable);
    List<AreaOfOperation> areaOfOperations = buildAreaOfOperation(id);
    List<ResidentialOfOperation> residentialOfOperations = buildResidentialOfOperation(id);
    return buidUserProfileResponse(user, posts, areaOfOperations, residentialOfOperations);
  }

  private UserProfileResponse buidUserProfileResponse(
      User user,
      Page<Post> posts,
      List<AreaOfOperation> areaOfOperations,
      List<ResidentialOfOperation> residentialOfOperations) {
    return UserProfileResponse.builder()
        .areaOfOperations(areaOfOperations)
        .residentialOfOperations(residentialOfOperations)
        .user(mapUserToUserResponse(user))
        .posts(new ResponsePage<>(posts))
        .build();
  }

  private List<AreaOfOperation> buildAreaOfOperation(Long userId) {
    return postRepository.findAreaOfOperationsByUserId(userId);
  }

  private List<ResidentialOfOperation> buildResidentialOfOperation(Long userId) {
    return postRepository.findResidentialOfOperationsByUserId(userId);
  }

  public User getUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> new ExceptionHandle(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.val()));
  }

  private UserResponse mapUserToUserResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .phoneNumber(user.getPhoneNumber())
        .avatar(user.getAvatar())
        .wallet(user.getWallet())
        .isAdmin(user.getIsAdmin())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
