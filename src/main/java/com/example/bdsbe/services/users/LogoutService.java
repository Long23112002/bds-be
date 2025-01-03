package com.example.bdsbe.services.users;

import com.example.bdsbe.exceptions.ErrorMessage;
import com.example.bdsbe.repositories.users.RefreshTokenRepository;
import com.longnh.exceptions.ExceptionHandle;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RedisTokenService redisTokenService;

  @Value("${jwt.key}")
  private String jwtKey;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader("Authorization");
    final String jwtRefreshToken;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    jwtRefreshToken = authHeader.substring(7);

    Claims claims = decodeJWT(jwtRefreshToken);
    long expirationTime = claims.getExpiration().getTime();

    var refreshToken =
        Optional.ofNullable(refreshTokenRepository.findByToken(jwtRefreshToken))
            .orElseThrow(
                () ->
                    new ExceptionHandle(
                        HttpStatus.NOT_FOUND, ErrorMessage.REFRESH_TOKEN_NOT_FOUND));

    redisTokenService.blacklistToken(jwtRefreshToken, expirationTime);

    refreshToken.setIsRevoked(true);
    refreshTokenRepository.save(refreshToken);

    String userId = redisTokenService.getUserIdFromRefreshToken(jwtRefreshToken);
    if (userId != null) {
      String accessToken = "access_token:" + userId;
      redisTokenService.deleteAccessToken(accessToken);
    }
  }

  private Claims decodeJWT(String jwt) {
    return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwt).getBody();
  }
}
