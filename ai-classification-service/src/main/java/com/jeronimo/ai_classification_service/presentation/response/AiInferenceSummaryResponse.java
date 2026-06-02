package com.jeronimo.ai_classification_service.presentation.response;

public record AiInferenceSummaryResponse(
        long totalInferences,
        long successfulInferences,
        long fallbackInferences,
        long failedInferences,
        double fallbackRate,
        double averageLatencyMs,
        String mostUsedModel
) { }
