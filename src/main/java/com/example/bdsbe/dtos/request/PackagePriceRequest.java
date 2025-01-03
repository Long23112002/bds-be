package com.example.bdsbe.dtos.request;

import com.example.bdsbe.enums.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackagePriceRequest {

  private Double price;

  private TimeUnit unit;

  private Long validity;
}
