package com.jeronimo.ai_classification_service.infrastructure.ai.ollama.dto;

public record OllamaGenerateResponse(
        String model,
        String response,
        boolean done
) { }
