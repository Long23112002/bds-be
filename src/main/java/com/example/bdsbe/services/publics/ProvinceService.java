package com.example.bdsbe.services.publics;

import com.example.bdsbe.entities.publics.Province;
import com.example.bdsbe.repositories.publics.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProvinceService {

  @Autowired private ProvinceRepository provinceRepository;

  public Page<Province> findAll(Pageable pageable) {
    return provinceRepository.findAll(pageable);
  }
}
