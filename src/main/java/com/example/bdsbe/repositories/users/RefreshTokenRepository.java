package com.example.bdsbe.repositories.users;

import com.example.bdsbe.entities.users.RefreshToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token")
  List<RefreshToken> findTokensByToken(@Param("token") String token);

  List<RefreshToken> findAllValidTokenByUserId(Long userId);

  RefreshToken findByToken(String token);

  void deleteByUserId(Long id);

  void deleteAllByUserId(Long id);
}
