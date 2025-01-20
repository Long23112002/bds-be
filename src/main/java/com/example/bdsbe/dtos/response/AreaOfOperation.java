package com.example.bdsbe.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaOfOperation {

  private String province;

  private String district;

  private Long countArena;
}
