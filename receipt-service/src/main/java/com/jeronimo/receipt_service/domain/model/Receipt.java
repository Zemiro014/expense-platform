package com.jeronimo.receipt_service.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Receipt {
    private UUID id;
    private String merchant;
    private BigDecimal amount;
    private ReceiptStatus status;
    private LocalDateTime createdAt;

    private Receipt() {}

    public static Receipt create(
            String merchant,
            BigDecimal amount) {

        Receipt receipt = new Receipt();

        receipt.id = UUID.randomUUID();
        receipt.merchant = merchant;
        receipt.amount = amount;
        receipt.status = ReceiptStatus.PENDING;
        receipt.createdAt = LocalDateTime.now();

        return receipt;
    }

    public static Receipt restore(
            UUID id,
            String merchant,
            BigDecimal amount,
            ReceiptStatus status,
            LocalDateTime createdAt) {

        Receipt receipt = new Receipt();

        receipt.id = id;
        receipt.merchant = merchant;
        receipt.amount = amount;
        receipt.status = status;
        receipt.createdAt = createdAt;

        return receipt;
    }

    public void approve() {

        if (status != ReceiptStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending receipts can be approved"
            );
        }

        this.status = ReceiptStatus.APPROVED;
    }

    public void reject() {

        if (status != ReceiptStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending receipts can be rejected"
            );
        }

        this.status = ReceiptStatus.REJECTED;
    }

    public boolean isApproved() {
        return status == ReceiptStatus.APPROVED;
    }

    public boolean isPending() {
        return status == ReceiptStatus.PENDING;
    }
}
