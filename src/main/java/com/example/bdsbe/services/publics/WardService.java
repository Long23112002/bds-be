package com.example.bdsbe.services.publics;

import com.example.bdsbe.entities.publics.Ward;
import com.example.bdsbe.repositories.publics.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WardService {

  @Autowired private WardRepository wardRepository;

  public Page<Ward> findByDistrictCode(String districtCode, Pageable pageable) {
    return wardRepository.findByDistrictCode(districtCode, pageable);
  }
}
