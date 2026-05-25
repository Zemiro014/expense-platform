package com.jeronimo.receipt_service.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptCreatedEvent(
        UUID receiptId,
        String merchant,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt,
        String correlationId
) { }
