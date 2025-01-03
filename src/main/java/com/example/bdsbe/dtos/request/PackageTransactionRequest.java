package com.example.bdsbe.dtos.request;

import com.example.bdsbe.entities.posts.PackagePrice;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageTransactionRequest {

  private Long postId;

  private PackagePrice packagePrice;

  private LocalDate startDate;

  private LocalDate endDate;

  private LocalTime startTime;
}
