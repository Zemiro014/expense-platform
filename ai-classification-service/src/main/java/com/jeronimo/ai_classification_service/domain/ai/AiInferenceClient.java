package com.jeronimo.ai_classification_service.domain.ai;

public interface AiInferenceClient {
    AiInferenceResult infer(AiInferenceRequest request);
}
