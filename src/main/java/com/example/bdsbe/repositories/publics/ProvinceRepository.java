package com.example.bdsbe.repositories.publics;

import com.example.bdsbe.entities.publics.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {

  Province findByCode(String code);
}
