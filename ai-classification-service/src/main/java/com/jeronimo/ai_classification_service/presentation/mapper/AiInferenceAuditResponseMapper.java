package com.jeronimo.ai_classification_service.presentation.mapper;

import com.jeronimo.ai_classification_service.domain.model.AiInferenceAudit;
import com.jeronimo.ai_classification_service.presentation.response.AiInferenceAuditResponse;

public class AiInferenceAuditResponseMapper {

    private AiInferenceAuditResponseMapper() {
    }

    public static AiInferenceAuditResponse toResponse(
            AiInferenceAudit audit
    ) {
        return new AiInferenceAuditResponse(
                audit.getId(),
                audit.getReceiptId(),
                audit.getUseCase(),
                audit.getModelName(),
                audit.getPromptVersion(),
                audit.getRawOutput(),
                audit.getNormalizedOutput(),
                audit.getLatencyMs(),
                audit.getFallbackUsed(),
                audit.getErrorMessage(),
                audit.getCreatedAt()
        );
    }
}
