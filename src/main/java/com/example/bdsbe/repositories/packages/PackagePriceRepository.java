package com.example.bdsbe.repositories.packages;

import com.example.bdsbe.entities.posts.PackagePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagePriceRepository extends JpaRepository<PackagePrice, Long> {}
