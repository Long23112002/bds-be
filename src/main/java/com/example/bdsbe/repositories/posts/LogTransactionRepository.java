package com.example.bdsbe.repositories.posts;

import com.example.bdsbe.entities.posts.LogsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogTransactionRepository extends JpaRepository<LogsTransaction, Long> {

  boolean existsByPackagePriceTransactionId(Long packagePriceTransactionId);
}
