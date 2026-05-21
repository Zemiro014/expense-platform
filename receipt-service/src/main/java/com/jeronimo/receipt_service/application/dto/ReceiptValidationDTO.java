package com.jeronimo.receipt_service.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ReceiptValidationDTO(
        UUID receiptId,
        BigDecimal amount
) {
}
