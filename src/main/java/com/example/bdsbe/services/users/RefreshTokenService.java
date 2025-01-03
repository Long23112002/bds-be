package com.example.bdsbe.services.users;

import com.example.bdsbe.dtos.response.AuthResponse;
import com.example.bdsbe.entities.users.RefreshToken;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.exceptions.ErrorMessage;
import com.example.bdsbe.repositories.users.RefreshTokenRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.longnh.exceptions.ExceptionHandle;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
  @Autowired private RefreshTokenRepository refreshTokenRepository;
  @Autowired private JwtService jwtService;
  @Autowired private UserRepository userRepository;

  public void saveUserToken(User user, String token) {
    var refreshToken = RefreshToken.builder().isRevoked(false).token(token).user(user).build();

    refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken findRefreshToken(String token) {
    return Optional.ofNullable(refreshTokenRepository.findByToken(token))
        .orElseThrow(
            () ->
                new ExceptionHandle(
                    HttpStatus.NOT_FOUND, ErrorMessage.REFRESH_TOKEN_NOT_FOUND.val()));
  }

  public void revokeAllUserToken(User user) {
    var validUserTokens = refreshTokenRepository.findAllValidTokenByUserId(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> token.setIsRevoked(true));
    refreshTokenRepository.saveAll(validUserTokens);
  }

  public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, ErrorMessage.INVALID_REFRESH_TOKEN.val());
    }

    final String refreshToken = authHeader.substring(7);
    final String refreshTokenKey = jwtService.getJwtRefreshKey();
    final String phoneNumber = jwtService.extractUserName(refreshToken, refreshTokenKey);

    User user =
        Optional.ofNullable(userRepository.findByPhoneNumber(phoneNumber))
            .orElseThrow(
                () -> new ExceptionHandle(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.val()));

    List<RefreshToken> tokens = refreshTokenRepository.findTokensByToken(refreshToken);
    if (tokens.isEmpty()) {
      throw new ExceptionHandle(HttpStatus.NOT_FOUND, ErrorMessage.REFRESH_TOKEN_NOT_FOUND.val());
    }

    RefreshToken existRefreshToken = tokens.get(0);

    if (!jwtService.isTokenValid(existRefreshToken.getToken(), user, refreshTokenKey)) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, ErrorMessage.INVALID_REFRESH_TOKEN.val());
    }

    String newAccessToken = jwtService.generateToken(user);
    String newRefreshToken =
        jwtService.generateNewRefreshTokenWithOldExpiryTime(existRefreshToken.getToken(), user);

    revokeAllUserToken(user);
    saveUserToken(user, newRefreshToken);

    return AuthResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();
  }
}
