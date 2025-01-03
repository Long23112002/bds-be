package com.example.bdsbe.controllers;

import com.example.bdsbe.entities.publics.District;
import com.example.bdsbe.services.publics.DistrictService;
import com.example.bdsbe.utils.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/districts")
public class DistrictController {

  @Autowired private DistrictService districtService;

  @GetMapping("/{provinceCode}")
  public ResponsePage<District> filter(@PathVariable String provinceCode, Pageable pageable) {
    return new ResponsePage<>(districtService.findByProvinceCode(provinceCode, pageable));
  }
}
