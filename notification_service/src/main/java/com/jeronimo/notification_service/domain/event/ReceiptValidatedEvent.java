package com.jeronimo.notification_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptValidatedEvent(
        UUID receiptId,
        String status,
        String reason,
        LocalDateTime validateAt,
        String correlationId
) { }
