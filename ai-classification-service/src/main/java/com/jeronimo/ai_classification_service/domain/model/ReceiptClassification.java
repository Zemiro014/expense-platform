package com.jeronimo.ai_classification_service.domain.model;

import com.jeronimo.ai_classification_service.domain.ai.AiInferenceResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReceiptClassification {
    private UUID id;
    private UUID receiptId;
    private String merchant;
    private BigDecimal amount;
    private ExpenseCategory category;
    private Double confidence;
    private String reason;
    private LocalDateTime classifiedAt;
    private AiInferenceResult inferenceResult;

    public static ReceiptClassification create(
            UUID receiptId,
            String merchant,
            BigDecimal amount,
            ExpenseCategory category,
            Double confidence,
            String reason,
            AiInferenceResult inferenceResult
    ) {
        return ReceiptClassification.builder()
                .id(UUID.randomUUID())
                .receiptId(receiptId)
                .merchant(merchant)
                .amount(amount)
                .category(category)
                .confidence(confidence)
                .reason(reason)
                .inferenceResult(inferenceResult)
                .classifiedAt(LocalDateTime.now())
                .build();
    }
}
