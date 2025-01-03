package com.example.bdsbe.controllers;

import com.example.bdsbe.entities.publics.Ward;
import com.example.bdsbe.services.publics.WardService;
import com.example.bdsbe.utils.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wards")
public class WardController {

  @Autowired private WardService wardService;

  @GetMapping("/{districtCode}")
  public ResponsePage<Ward> findByDistrictCode(
      @PathVariable String districtCode, Pageable pageable) {
    return new ResponsePage<>(wardService.findByDistrictCode(districtCode, pageable));
  }
}
