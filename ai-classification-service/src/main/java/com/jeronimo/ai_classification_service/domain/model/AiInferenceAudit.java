package com.jeronimo.ai_classification_service.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AiInferenceAudit {

    private UUID id;
    private UUID receiptId;
    private String useCase;
    private String modelName;
    private String promptVersion;
    private String rawOutput;
    private String normalizedOutput;
    private Long latencyMs;
    private Boolean fallbackUsed;
    private String errorMessage;
    private LocalDateTime createdAt;

    public static AiInferenceAudit create(
        UUID receiptId,
        String useCase,
        String modelName,
        String promptVersion,
        String rawOutput,
        String normalizedOutput,
        Long latencyMs,
        Boolean fallbackUsed,
        String errorMessage
    ) {
        return AiInferenceAudit.builder()
            .id(UUID.randomUUID())
            .receiptId(receiptId)
            .useCase(useCase)
            .modelName(modelName)
            .promptVersion(promptVersion)
            .rawOutput(rawOutput)
            .normalizedOutput(normalizedOutput)
            .latencyMs(latencyMs)
            .fallbackUsed(fallbackUsed)
            .errorMessage(errorMessage)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
