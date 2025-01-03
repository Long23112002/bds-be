package com.example.bdsbe.dtos.request;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageBuyRequest {
  @NotNull private Long packagePriceId;

  private Double totalPrice;

  private LocalDate startDate;

  private LocalTime startTime;
}
