package com.jeronimo.ai_classification_service.domain.ai;

public record AiInferenceResult(
        String modelName,
        String promptVersion,
        String rawOutput,
        long latencyMs,
        boolean fallbackUsed,
        String errorMessage
){ }
