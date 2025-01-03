package com.example.bdsbe.repositories.users;

import com.example.bdsbe.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByPhoneNumber(String phoneNumber);

  @Query(value = "select last_value + 1 from users.user_id_seq", nativeQuery = true)
  long seq();
}
