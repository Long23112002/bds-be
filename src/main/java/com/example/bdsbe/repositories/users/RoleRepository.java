package com.example.bdsbe.repositories.users;

import com.example.bdsbe.entities.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByCode(String code);
}
