package com.jeronimo.ai_classification_service.infrastructure.ai.ollama;

import com.jeronimo.ai_classification_service.domain.ai.AiInferenceClient;
import com.jeronimo.ai_classification_service.domain.ai.AiInferenceRequest;
import com.jeronimo.ai_classification_service.domain.ai.AiInferenceResult;
import com.jeronimo.ai_classification_service.infrastructure.ai.ollama.dto.OllamaGenerateRequest;
import com.jeronimo.ai_classification_service.infrastructure.ai.ollama.dto.OllamaGenerateResponse;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class OllamaAiInferenceClient implements AiInferenceClient {

    private final RestClient ollamaRestClient;
    private final ObservationRegistry observationRegistry;
    private final String model;

    public OllamaAiInferenceClient(
            RestClient ollamaRestClient,
            ObservationRegistry observationRegistry,
            @Value("${app.ai.ollama.model}") String model
    ) {
        this.ollamaRestClient = ollamaRestClient;
        this.observationRegistry = observationRegistry;
        this.model = model;
    }

    @Override
    public AiInferenceResult infer(AiInferenceRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            MDC.put("event", "ai_inference_started");
            MDC.put("aiProvider", "ollama");
            MDC.put("aiModel", model);
            MDC.put("aiUseCase", request.useCase());
            MDC.put("promptVersion", request.promptVersion());

            log.info("AI inference started");

            OllamaGenerateResponse response = Observation
                .createNotStarted("ollama.generate", observationRegistry)
                .lowCardinalityKeyValue("ai.provider", "ollama")
                .lowCardinalityKeyValue("ai.model", model)
                .lowCardinalityKeyValue("ai.use_case", request.useCase())
                .lowCardinalityKeyValue("prompt.version", request.promptVersion())
                .observe(() ->
                    ollamaRestClient
                        .post()
                        .uri("/api/generate")
                        .body(new OllamaGenerateRequest(
                                model,
                                request.prompt(),
                                false
                        ))
                        .retrieve()
                        .body(OllamaGenerateResponse.class)
                );

            long latencyMs = System.currentTimeMillis() - startTime;

            String rawOutput = response == null
                    ? ""
                    : response.response();

            MDC.put("event", "ai_inference_completed");
            MDC.put("latencyMs", String.valueOf(latencyMs));

            log.info("AI inference completed");

            return new AiInferenceResult(
                request.useCase(),
                model,
                request.promptVersion(),
                rawOutput,
                latencyMs,
                false,
                null
            );

        } catch (Exception ex) {
            long latencyMs = System.currentTimeMillis() - startTime;

            MDC.put("event", "ai_inference_failed");
            MDC.put("latencyMs", String.valueOf(latencyMs));

            log.error("AI inference failed", ex);

            return new AiInferenceResult(
                request.useCase(),
                model,
                request.promptVersion(),
                "",
                latencyMs,
                true,
                ex.getMessage()
            );

        } finally {
            MDC.remove("event");
            MDC.remove("aiProvider");
            MDC.remove("aiModel");
            MDC.remove("aiUseCase");
            MDC.remove("promptVersion");
            MDC.remove("latencyMs");
        }
    }
}
