package com.jeronimo.validation_service.domain.repository;

import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptValidationRepository {
    ReceiptValidation save(ReceiptValidation validation);

    List<ReceiptValidation> findAll();

    Optional<ReceiptValidation> findByReceiptId(UUID receiptId);

    boolean existsByReceiptId(UUID receiptId);
}
