package com.jeronimo.ai_classification_service.application.usecase;

import com.jeronimo.ai_classification_service.domain.model.AiInferenceAudit;
import com.jeronimo.ai_classification_service.domain.repository.AiInferenceAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindAiInferenceAuditUseCase {

    private final AiInferenceAuditRepository repository;

    public List<AiInferenceAudit> findAll() {
        return repository.findAll();
    }

    public List<AiInferenceAudit> findByReceiptId(UUID receiptId) {
        return repository.findByReceiptId(receiptId);
    }
}
