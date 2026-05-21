package com.jeronimo.receipt_service.presentation.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ReceiptResponse(
        UUID id,
        String merchant,
        BigDecimal amount,
        String status
) {}
