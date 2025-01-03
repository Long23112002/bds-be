package com.example.bdsbe.services.users;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

  @Autowired private RedisTemplate<String, Object> redisTemplate;
  @Autowired private JwtService jwtService;

  // Lưu Access Token
  public void saveAccessToken(String token, String userId, long expirationTime) {
    redisTemplate
        .opsForValue()
        .set("access_token:" + userId, token, expirationTime, TimeUnit.MILLISECONDS);
  }

  // Lưu Refresh Token theo userId
  public void saveRefreshToken(String token, String userId, long expirationTime) {
    redisTemplate
        .opsForValue()
        .set("refresh_token:" + userId, token, expirationTime, TimeUnit.MILLISECONDS);
  }

  // Lấy User ID từ Access Token
  // Lấy Access Token theo userId
  public String getAccessTokenByUserId(String userId) {
    return (String) redisTemplate.opsForValue().get("access_token:" + userId);
  }

  // Lấy Refresh Token theo userId
  public String getRefreshTokenByUserId(String userId) {
    return (String) redisTemplate.opsForValue().get("refresh_token:" + userId);
  }

  public String getUserIdFromRefreshToken(String token) {
    return (String) redisTemplate.opsForValue().get("refresh_token:" + token);
  }

  // Xóa Access Token
  public void deleteAccessToken(String token) {
    redisTemplate.delete("access_token:" + token);
  }

  // Xóa Refresh Token
  public void deleteRefreshToken(String token) {
    redisTemplate.delete("refresh_token:" + token);
  }

  // Thêm token vào blacklist
  public void blacklistToken(String token, long expirationTime) {
    redisTemplate
        .opsForValue()
        .set("blacklist:" + token, true, expirationTime, TimeUnit.MILLISECONDS);
  }

  // Kiểm tra token có trong blacklist hay không
  public boolean isTokenBlacklisted(String token) {
    Boolean isBlacklisted = (Boolean) redisTemplate.opsForValue().get("blacklist:" + token);
    return isBlacklisted != null && isBlacklisted;
  }
}
