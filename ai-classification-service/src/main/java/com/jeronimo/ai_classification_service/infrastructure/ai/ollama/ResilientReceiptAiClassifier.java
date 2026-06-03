package com.jeronimo.ai_classification_service.infrastructure.ai.ollama;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.service.ReceiptAiClassifier;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component("resilientReceiptAiClassifier")
public class ResilientReceiptAiClassifier implements ReceiptAiClassifier {

    private final ReceiptAiClassifier ollamaClassifier;
    private final ReceiptAiClassifier ruleBasedClassifier;

    public ResilientReceiptAiClassifier(
            @Qualifier("ollamaReceiptAiClassifier")
            ReceiptAiClassifier ollamaClassifier,

            @Qualifier("ruleBasedReceiptAiClassifier")
            ReceiptAiClassifier ruleBasedClassifier
    ) {
        this.ollamaClassifier = ollamaClassifier;
        this.ruleBasedClassifier = ruleBasedClassifier;
    }

    @Override
    @CircuitBreaker(
            name = "ollamaAi",
            fallbackMethod = "fallbackClassify"
    )
    @Retry(name = "ollamaAi")
    public ReceiptClassification classify(
            UUID receiptId,
            String merchant,
            BigDecimal amount
    ) {
        return ollamaClassifier.classify(
                receiptId,
                merchant,
                amount
        );
    }

    public ReceiptClassification fallbackClassify(
            UUID receiptId,
            String merchant,
            BigDecimal amount,
            Throwable throwable
    ) {
        MDC.put("event", "ai_fallback_triggered");
        MDC.put("fallbackProvider", "rule-based");

        try {
            log.warn("Ollama AI failed. Falling back to rule-based classifier", throwable);

            ReceiptClassification fallbackClassification =
                    ruleBasedClassifier.classify(receiptId, merchant, amount);

            return ReceiptClassification.create(
                    receiptId,
                    merchant,
                    amount,
                    fallbackClassification.getCategory(),
                    0.40,
                    "Fallback rule-based classification used because Ollama failed: "
                            + throwable.getClass().getSimpleName(),
                    fallbackClassification.getInferenceResult()
            );

        } finally {
            MDC.remove("event");
            MDC.remove("fallbackProvider");
        }
    }
}
