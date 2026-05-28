package com.jeronimo.ai_classification_service.domain.model;

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

    public static ReceiptClassification create(
            UUID receiptId,
            String merchant,
            BigDecimal amount,
            ExpenseCategory category,
            Double confidence,
            String reason
    ) {
        return ReceiptClassification.builder()
                .id(UUID.randomUUID())
                .receiptId(receiptId)
                .merchant(merchant)
                .amount(amount)
                .category(category)
                .confidence(confidence)
                .reason(reason)
                .classifiedAt(LocalDateTime.now())
                .build();
    }
}
