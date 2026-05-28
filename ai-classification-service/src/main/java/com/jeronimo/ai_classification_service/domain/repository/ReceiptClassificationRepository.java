package com.jeronimo.ai_classification_service.domain.repository;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptClassificationRepository {

    ReceiptClassification save(ReceiptClassification classification);

    boolean existsByReceiptId(UUID receiptId);

    List<ReceiptClassification> findAll();

    Optional<ReceiptClassification> findByReceiptId(UUID receiptId);
}
