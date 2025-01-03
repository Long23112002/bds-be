package com.example.bdsbe.dtos.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequest {

  private String name;

  private Integer level;

  private List<PackagePriceRequest> packagePrices;

  private String description;
}
