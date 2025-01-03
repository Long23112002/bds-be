package com.example.bdsbe.controllers;

import com.example.bdsbe.entities.publics.Province;
import com.example.bdsbe.services.publics.ProvinceService;
import com.example.bdsbe.utils.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provinces")
public class ProvinceController {

  @Autowired private ProvinceService provinceService;

  @GetMapping
  public ResponsePage<Province> filter(Pageable pageable) {
    return new ResponsePage<>(provinceService.findAll(pageable));
  }
}
