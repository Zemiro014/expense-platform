package com.jeronimo.ai_classification_service.presentation.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AiInferenceAuditResponse(
    UUID id,
    UUID receiptId,
    String useCase,
    String modelName,
    String promptVersion,
    String rawOutput,
    String normalizedOutput,
    Long latencyMs,
    Boolean fallbackUsed,
    String errorMessage,
    LocalDateTime createdAt
) { }
