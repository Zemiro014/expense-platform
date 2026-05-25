package com.jeronimo.validation_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptValidatedEvent(
        UUID receiptId,
        String status,
        String reason,
        LocalDateTime validatedAt,
        String correlationId
) {
}
