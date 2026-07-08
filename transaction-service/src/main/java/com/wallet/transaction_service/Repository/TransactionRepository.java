package com.wallet.transaction_service.Repository;

import com.wallet.transaction_service.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
}
