package com.jeronimo.ai_classification_service.domain.ai;

import java.util.Map;

public record AiInferenceRequest(
        String useCase,
        String promptVersion,
        String prompt,
        Map<String, Object> metadata
) { }
