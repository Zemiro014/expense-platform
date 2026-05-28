package com.jeronimo.ai_classification_service.infrastructure.ia.ollama.dto;

public record OllamaGenerateRequest(
        String model,
        String prompt,
        boolean stream
) { }
