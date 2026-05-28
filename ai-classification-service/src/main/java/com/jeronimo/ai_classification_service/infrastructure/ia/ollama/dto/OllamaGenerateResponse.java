package com.jeronimo.ai_classification_service.infrastructure.ia.ollama.dto;

public record OllamaGenerateResponse(
        String model,
        String response,
        boolean done
) { }
