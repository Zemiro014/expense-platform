package com.jeronimo.ai_classification_service.application.usecase;

import com.jeronimo.ai_classification_service.infrastructure.persistence.repository.SpringDataAiInferenceAuditRepository;
import com.jeronimo.ai_classification_service.presentation.response.AiInferenceSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAiInferenceSummaryUseCase {

    private final SpringDataAiInferenceAuditRepository repository;

    public AiInferenceSummaryResponse execute() {
        long total = repository.count();
        long fallback = repository.countByFallbackUsedTrue();
        long failed = repository.countByErrorMessageIsNotNull();
        long successful = total - failed;

        double fallbackRate = total == 0
                ? 0.0
                : (double) fallback / total;

        Double avgLatency = repository.averageLatencyMs();

        String mostUsedModel = repository.findMostUsedModel();

        return new AiInferenceSummaryResponse(
                total,
                successful,
                fallback,
                failed,
                fallbackRate,
                avgLatency == null ? 0.0 : avgLatency,
                mostUsedModel == null ? "N/A" : mostUsedModel
        );
    }
}
