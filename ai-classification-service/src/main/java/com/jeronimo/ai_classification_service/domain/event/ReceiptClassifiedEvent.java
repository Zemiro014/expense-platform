package com.jeronimo.ai_classification_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptClassifiedEvent(
        UUID receiptId,
        String category,
        Double confidence,
        String reason,
        LocalDateTime classifiedAt,
        String correlationId
) { }
