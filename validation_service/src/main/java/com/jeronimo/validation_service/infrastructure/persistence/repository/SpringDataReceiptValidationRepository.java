package com.jeronimo.validation_service.infrastructure.persistence.repository;

import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataReceiptValidationRepository extends JpaRepository<ReceiptValidationEntity, UUID> {
    Optional<ReceiptValidationEntity> findByReceiptId(UUID receiptId);

    boolean existsByReceiptId(UUID receiptId);
}
