package com.jeronimo.ai_classification_service.presentation.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptClassificationResponse(
        UUID id,
        UUID receiptId,
        String merchant,
        BigDecimal amount,
        String category,
        Double confidence,
        String reason,
        LocalDateTime classifiedAt
) {
}