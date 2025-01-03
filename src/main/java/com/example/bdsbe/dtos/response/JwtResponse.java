package com.example.bdsbe.dtos.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

  private List<String> authoritiesSystem;
  private Long userId;
  private String sub;
}
