package com.jeronimo.validation_service.presentation.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReceiptValidationResponse(
        UUID id,
        UUID receiptId,
        String merchant,
        BigDecimal amount,
        String status,
        String reason,
        LocalDateTime validateAt
){ }
