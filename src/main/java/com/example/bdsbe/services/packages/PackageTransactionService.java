package com.example.bdsbe.services.packages;

import com.example.bdsbe.dtos.request.PostRequest;
import com.example.bdsbe.dtos.response.JwtResponse;
import com.example.bdsbe.entities.posts.PackagePrice;
import com.example.bdsbe.entities.posts.PackagePriceTransaction;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.enums.TimeUnit;
import com.example.bdsbe.repositories.packages.PackagePriceRepository;
import com.example.bdsbe.repositories.packages.PackagePriceTransactionRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.example.bdsbe.services.users.JwtService;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PackageTransactionService {

  @Autowired private PackagePriceTransactionRepository packagePriceTransactionRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private PackagePriceRepository packagePriceRepository;

  @Autowired private JwtService jwtService;

  @Transactional(rollbackFor = Exception.class)
  public PackagePriceTransaction save(PostRequest request, String token, Long postId) {
    PackagePriceTransaction packagePriceTransaction = new PackagePriceTransaction();
    PackagePrice packagePrice = getPackagePrice(request.getPackageBuy().getPackagePriceId());
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    System.out.println(jwtResponse.getUserId());
    if (validateTransaction(jwtResponse.getUserId(), packagePrice)) {
      FnCommon.copyProperties(packagePriceTransaction, request);
      packagePriceTransaction.setStatus(PostStatus.PENDING);
      packagePriceTransaction.setStartDate(request.getPackageBuy().getStartDate());
      packagePriceTransaction.setEndDate(
          getEndDate(request.getPackageBuy().getStartDate(), packagePrice));
      packagePriceTransaction.setStartTime(request.getPackageBuy().getStartTime());
      packagePriceTransaction.setPostId(postId);
      packagePriceTransaction.setPackagePrice(
          getPackagePrice(request.getPackageBuy().getPackagePriceId()));
      packagePriceTransactionRepository.save(packagePriceTransaction);
    }
    return packagePriceTransaction;
  }

  private LocalDate getEndDate(LocalDate startDate, PackagePrice packagePrice) {
    TimeUnit unit = packagePrice.getUnit();
    long validity = packagePrice.getValidity();

    return switch (unit) {
      case DAYS -> startDate.plusDays(validity);
      case MONTHS -> startDate.plusMonths(validity);
      case YEARS -> startDate.plusYears(validity);
      default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
    };
  }

  private boolean validateTransaction(Long userId, PackagePrice packagePrice) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy user"));

    if (user.getWallet() < packagePrice.getPrice()) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, "Số dư không đủ");
    } else {
      user.setWallet(user.getWallet() - packagePrice.getPrice());
      userRepository.save(user);
    }
    return true;
  }

  private PackagePrice getPackagePrice(Long packagePriceId) {
    return packagePriceRepository
        .findById(packagePriceId)
        .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy gói cước"));
  }
}
