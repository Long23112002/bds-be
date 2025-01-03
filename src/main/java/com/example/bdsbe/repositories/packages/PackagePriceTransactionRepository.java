package com.example.bdsbe.repositories.packages;

import com.example.bdsbe.entities.posts.PackagePriceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagePriceTransactionRepository
    extends JpaRepository<PackagePriceTransaction, Long> {

  PackagePriceTransaction findByPostId(Long postId);
}
