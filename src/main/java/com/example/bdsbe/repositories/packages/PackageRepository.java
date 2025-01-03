package com.example.bdsbe.repositories.packages;

import com.example.bdsbe.entities.posts.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

  @Query("SELECT p FROM Package p")
  Page<Package> filter(Pageable pageable);
}
