package com.jeronimo.ai_classification_service.application.usecase;

import com.jeronimo.ai_classification_service.application.mapper.ReceiptClassifiedEventMapper;
import com.jeronimo.ai_classification_service.domain.ai.AiInferenceResult;
import com.jeronimo.ai_classification_service.domain.event.ReceiptCreatedEvent;
import com.jeronimo.ai_classification_service.domain.model.AiInferenceAudit;
import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.publisher.ReceiptClassificationEventPublisher;
import com.jeronimo.ai_classification_service.domain.repository.AiInferenceAuditRepository;
import com.jeronimo.ai_classification_service.domain.repository.ReceiptClassificationRepository;
import com.jeronimo.ai_classification_service.domain.service.ReceiptAiClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClassifyReceiptUseCase {

    private final ReceiptAiClassifier receiptAiClassifier;
    private final ReceiptClassificationRepository repository;
    private final ReceiptClassificationEventPublisher eventPublisher;
    private final AiInferenceAuditRepository auditRepository;

    public ClassifyReceiptUseCase(
            @Qualifier("resilientReceiptAiClassifier")
            ReceiptAiClassifier receiptAiClassifier,
            ReceiptClassificationRepository repository,
            ReceiptClassificationEventPublisher eventPublisher,
            AiInferenceAuditRepository auditRepository
    ) {
        this.receiptAiClassifier = receiptAiClassifier;
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.auditRepository = auditRepository;
    }

    public void execute(ReceiptCreatedEvent event) {

        if (repository.existsByReceiptId(event.receiptId())) {
            MDC.put("event", "receipt_classification_skipped");
            log.info("Receipt already classified. Skipping duplicated event");
            MDC.remove("event");
            return;
        }

        ReceiptClassification classification =
            receiptAiClassifier.classify(
                event.receiptId(),
                event.merchant(),
                event.amount()
            );

        ReceiptClassification saved =
                repository.save(classification);

        MDC.put("event", "receipt_classified");
        MDC.put("category", saved.getCategory().name());
        MDC.put("confidence", String.valueOf(saved.getConfidence()));

        log.info("Receipt classified successfully");

        MDC.remove("event");
        MDC.remove("category");
        MDC.remove("confidence");

        AiInferenceResult inferenceResult = classification.getInferenceResult();
        if(inferenceResult != null) {
            AiInferenceAudit audit = AiInferenceAudit.create(
                    classification.getReceiptId(),
                    inferenceResult.useCase(),
                    inferenceResult.modelName(),
                    inferenceResult.promptVersion(),
                    inferenceResult.rawOutput(),
                    classification.getCategory().name(),
                    inferenceResult.latencyMs(),
                    inferenceResult.fallbackUsed(),
                    inferenceResult.errorMessage()
            );
            auditRepository.save(audit);
        }

        eventPublisher.publishClassified(
                ReceiptClassifiedEventMapper.toEvent(
                        saved,
                        event.correlationId()
                )
        );
    }
}
