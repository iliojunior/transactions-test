package com.ilioadriano.transactions.domain.repository;

import com.ilioadriano.transactions.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to database operations on {@link Transaction}
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
