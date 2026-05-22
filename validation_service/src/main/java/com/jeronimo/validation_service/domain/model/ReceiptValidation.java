package com.jeronimo.validation_service.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ReceiptValidation {
    private UUID id;
    private UUID receiptId;
    private String merchant;
    private BigDecimal amount;
    private ValidationStatus status;
    private String reason;
    private LocalDateTime validatedAt;

    private ReceiptValidation() {
    }

    public static ReceiptValidation validate(
            UUID receiptId,
            String merchant,
            BigDecimal amount
    ) {
        ReceiptValidation validation = new ReceiptValidation();

        validation.id = UUID.randomUUID();
        validation.receiptId = receiptId;
        validation.merchant = merchant;
        validation.amount = amount;
        validation.validatedAt = LocalDateTime.now();

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            validation.status = ValidationStatus.REJECTED;
            validation.reason = "Amount exceeds allowed limit";
        } else {
            validation.status = ValidationStatus.APPROVED;
            validation.reason = "Receipt approved";
        }

        return validation;
    }

    public static ReceiptValidation restore(
            UUID id,
            UUID receiptId,
            String merchant,
            BigDecimal amount,
            ValidationStatus status,
            String reason,
            LocalDateTime validatedAt
    ) {
        ReceiptValidation validation = new ReceiptValidation();

        validation.id = id;
        validation.receiptId = receiptId;
        validation.merchant = merchant;
        validation.amount = amount;
        validation.status = status;
        validation.reason = reason;
        validation.validatedAt = validatedAt;

        return validation;
    }
}
