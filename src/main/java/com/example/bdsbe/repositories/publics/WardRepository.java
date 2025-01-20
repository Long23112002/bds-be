package com.example.bdsbe.repositories.publics;

import com.example.bdsbe.entities.publics.Ward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {

  Page<Ward> findByDistrictCode(String districtCode, Pageable pageable);

  Ward findByCode(String code);
}
