package com.jeronimo.receipt_service.infrastructure.persistence.mapper;

import com.jeronimo.receipt_service.domain.model.Receipt;
import com.jeronimo.receipt_service.infrastructure.persistence.entity.ReceiptEntity;

public class ReceiptEntityMapper {
    private ReceiptEntityMapper() {
    }

    public static ReceiptEntity toEntity(Receipt receipt) {

        ReceiptEntity entity = new ReceiptEntity();

        entity.setId(receipt.getId());
        entity.setMerchant(receipt.getMerchant());
        entity.setAmount(receipt.getAmount());
        entity.setStatus(receipt.getStatus());
        entity.setCreatedAt(receipt.getCreatedAt());

        return entity;
    }

    public static Receipt toDomain(ReceiptEntity entity) {
        entity.markNotNew();
        return Receipt.restore(
                entity.getId(),
                entity.getMerchant(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
