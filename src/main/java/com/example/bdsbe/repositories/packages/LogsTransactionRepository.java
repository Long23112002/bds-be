package com.example.bdsbe.repositories.packages;

import com.example.bdsbe.entities.posts.LogsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsTransactionRepository extends JpaRepository<LogsTransaction, Long> {}
