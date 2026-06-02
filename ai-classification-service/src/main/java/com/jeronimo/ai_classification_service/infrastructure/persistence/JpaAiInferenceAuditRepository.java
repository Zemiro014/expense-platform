package com.jeronimo.ai_classification_service.infrastructure.persistence;

import com.jeronimo.ai_classification_service.domain.model.AiInferenceAudit;
import com.jeronimo.ai_classification_service.domain.repository.AiInferenceAuditRepository;
import com.jeronimo.ai_classification_service.infrastructure.persistence.entity.AiInferenceAuditEntity;
import com.jeronimo.ai_classification_service.infrastructure.persistence.mapper.AiInferenceAuditEntityMapper;
import com.jeronimo.ai_classification_service.infrastructure.persistence.repository.SpringDataAiInferenceAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaAiInferenceAuditRepository implements AiInferenceAuditRepository {
    private final SpringDataAiInferenceAuditRepository repository;

    @Override
    public AiInferenceAudit save(AiInferenceAudit audit) {
        AiInferenceAuditEntity entity = AiInferenceAuditEntityMapper.toEntity(audit);

        AiInferenceAuditEntity saved = repository.save(entity);

        return AiInferenceAuditEntityMapper.toDomain(saved);
    }

    @Override
    public List<AiInferenceAudit> findAll() {
        return repository.findAll()
                .stream()
                .map(AiInferenceAuditEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<AiInferenceAudit> findByReceiptId(UUID receiptId) {
        return repository.findByReceiptId(receiptId)
                .stream()
                .map(AiInferenceAuditEntityMapper::toDomain)
                .toList();
    }
}
