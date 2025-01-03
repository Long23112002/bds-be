package com.example.bdsbe.dtos.response;

import com.example.bdsbe.entities.users.Role;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

  private Long id;

  private String email;

  private String fullName;

  private String phoneNumber;

  private String avatar;

  private Boolean isAdmin;

  private Double wallet;

  private Set<Role> roles;
}
