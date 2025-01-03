package com.example.bdsbe.services.publics;

import com.example.bdsbe.entities.publics.District;
import com.example.bdsbe.repositories.publics.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

  @Autowired private DistrictRepository districtRepository;

  public Page<District> findByProvinceCode(String provinceCode, Pageable pageable) {
    return districtRepository.findByProvinceCode(provinceCode, pageable);
  }
}
