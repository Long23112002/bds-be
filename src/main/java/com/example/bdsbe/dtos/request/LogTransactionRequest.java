package com.example.bdsbe.dtos.request;

import com.example.bdsbe.entities.posts.PackagePriceTransaction;
import com.example.bdsbe.enums.PackageStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTransactionRequest {

  private PackagePriceTransaction packagePriceTransaction;

  private Long userApproved;

  private Double price;

  private PackageStatus status;

  private PackageStatus oldStatus;
}
