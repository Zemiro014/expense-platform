package com.jeronimo.ai_classification_service.infrastructure.persistence.mapper;

import com.jeronimo.ai_classification_service.domain.model.AiInferenceAudit;
import com.jeronimo.ai_classification_service.infrastructure.persistence.entity.AiInferenceAuditEntity;

public class AiInferenceAuditEntityMapper {

    private AiInferenceAuditEntityMapper() {
    }

    public static AiInferenceAuditEntity toEntity(AiInferenceAudit audit) {
        AiInferenceAuditEntity entity = new AiInferenceAuditEntity();

        entity.setId(audit.getId());
        entity.setReceiptId(audit.getReceiptId());
        entity.setUseCase(audit.getUseCase());
        entity.setModelName(audit.getModelName());
        entity.setPromptVersion(audit.getPromptVersion());
        entity.setRawOutput(audit.getRawOutput());
        entity.setNormalizedOutput(audit.getNormalizedOutput());
        entity.setLatencyMs(audit.getLatencyMs());
        entity.setFallbackUsed(audit.getFallbackUsed());
        entity.setErrorMessage(audit.getErrorMessage());
        entity.setCreatedAt(audit.getCreatedAt());

        return entity;
    }

    public static AiInferenceAudit toDomain(AiInferenceAuditEntity entity) {
        return AiInferenceAudit.builder()
                .id(entity.getId())
                .receiptId(entity.getReceiptId())
                .useCase(entity.getUseCase())
                .modelName(entity.getModelName())
                .promptVersion(entity.getPromptVersion())
                .rawOutput(entity.getRawOutput())
                .normalizedOutput(entity.getNormalizedOutput())
                .latencyMs(entity.getLatencyMs())
                .fallbackUsed(entity.getFallbackUsed())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}