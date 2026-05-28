package com.jeronimo.ai_classification_service.infrastructure.persistence.repository;

import com.jeronimo.ai_classification_service.infrastructure.persistence.entity.ReceiptClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataReceiptClassificationRepository extends JpaRepository<ReceiptClassificationEntity, UUID> {
    boolean existsByReceiptId(UUID receiptId);

    Optional<ReceiptClassificationEntity> findByReceiptId(UUID receiptId);
}
