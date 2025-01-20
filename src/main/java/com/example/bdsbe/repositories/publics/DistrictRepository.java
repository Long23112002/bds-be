package com.example.bdsbe.repositories.publics;

import com.example.bdsbe.entities.publics.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {

  Page<District> findByProvinceCode(String provinceCode, Pageable pageable);

  District findByCode(String code);
}
