package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.request.PackageRequest;
import com.example.bdsbe.entities.posts.Package;
import com.example.bdsbe.services.packages.PackageService;
import com.example.bdsbe.utils.ResponsePage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

  @Autowired private PackageService packageService;

  @PostMapping
  public Package save(@Valid @RequestBody PackageRequest request) {
    return packageService.save(request);
  }

  @GetMapping
  public ResponsePage<Package> findAll(Pageable pageable) {
    return new ResponsePage<>(packageService.filter(pageable));
  }
}
