package com.ilioadriano.transactions.domain.repository;

import com.ilioadriano.transactions.domain.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository layer to database operations on {@link OperationType}
 */
@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Integer> {
}
