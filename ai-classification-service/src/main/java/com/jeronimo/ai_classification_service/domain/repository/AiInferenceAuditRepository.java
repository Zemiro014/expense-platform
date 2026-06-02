package com.jeronimo.ai_classification_service.domain.repository;

import com.jeronimo.ai_classification_service.domain.model.AiInferenceAudit;

import java.util.List;
import java.util.UUID;

public interface AiInferenceAuditRepository {
    AiInferenceAudit save(AiInferenceAudit audit);

    List<AiInferenceAudit> findAll();

    List<AiInferenceAudit> findByReceiptId(UUID receiptId);
}
