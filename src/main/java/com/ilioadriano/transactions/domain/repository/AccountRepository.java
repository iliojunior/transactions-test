package com.ilioadriano.transactions.domain.repository;

import com.ilioadriano.transactions.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to database operations on {@link Account}
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
