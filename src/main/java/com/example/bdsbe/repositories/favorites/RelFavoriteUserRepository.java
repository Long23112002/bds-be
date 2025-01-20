package com.example.bdsbe.repositories.favorites;

import com.example.bdsbe.entities.favorites.RelFavoriteUser;
import com.example.bdsbe.entities.primaryKey.RelFavoriteUserId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelFavoriteUserRepository
    extends JpaRepository<RelFavoriteUser, RelFavoriteUserId> {

  List<RelFavoriteUser> findAllByIdUserId(Long userId);
}
