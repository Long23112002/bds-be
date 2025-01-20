package com.example.bdsbe.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceCountResponse {

  private String code;

  private String provinceName;

  private Long count;
}
