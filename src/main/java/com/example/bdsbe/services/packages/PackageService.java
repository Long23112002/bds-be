package com.example.bdsbe.services.packages;

import com.example.bdsbe.dtos.request.PackagePriceRequest;
import com.example.bdsbe.dtos.request.PackageRequest;
import com.example.bdsbe.entities.posts.Package;
import com.example.bdsbe.entities.posts.PackagePrice;
import com.example.bdsbe.repositories.packages.PackagePriceRepository;
import com.example.bdsbe.repositories.packages.PackageRepository;
import com.longnh.exceptions.ExceptionHandle;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PackageService {

  @Autowired private PackageRepository packageRepository;

  @Autowired private PackagePriceRepository packagePriceRepository;

  @Transactional
  public Package save(PackageRequest request) {
    Package apackage = new Package();
    apackage.setName(request.getName());
    apackage.setLevel(request.getLevel());
    apackage.setDescription(request.getDescription());

    apackage = packageRepository.save(apackage);
    apackage.setPackagePriceList(savePackagePrice(apackage, request));
    return apackage;
  }

  private List<PackagePrice> savePackagePrice(Package packages, PackageRequest req) {
    List<PackagePrice> list = new ArrayList<>();
    for (PackagePriceRequest price : req.getPackagePrices()) {
      PackagePrice packagePrice = new PackagePrice();
      packagePrice.setPrice(price.getPrice());
      packagePrice.setUnit(price.getUnit());
      packagePrice.setAPackage(packages);
      packagePrice.setValidity(price.getValidity());
      list.add(packagePrice);
      packagePriceRepository.save(packagePrice);
    }
    return list;
  }

  public Package getPackageById(Long id) {
    return packageRepository
        .findById(id)
        .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy gói"));
  }

  public Page<Package> filter(Pageable pageable) {
    return packageRepository.filter(pageable);
  }
}
