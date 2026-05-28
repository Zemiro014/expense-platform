package com.jeronimo.ai_classification_service.infrastructure.persistence.mapper;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.infrastructure.persistence.entity.ReceiptClassificationEntity;

public class ReceiptClassificationEntityMapper {

    private ReceiptClassificationEntityMapper() {
    }

    public static ReceiptClassificationEntity toEntity(
            ReceiptClassification classification
    ) {
        ReceiptClassificationEntity entity =
                new ReceiptClassificationEntity();

        entity.setId(classification.getId());
        entity.setReceiptId(classification.getReceiptId());
        entity.setMerchant(classification.getMerchant());
        entity.setAmount(classification.getAmount());
        entity.setCategory(classification.getCategory());
        entity.setConfidence(classification.getConfidence());
        entity.setReason(classification.getReason());
        entity.setClassifiedAt(classification.getClassifiedAt());

        return entity;
    }

    public static ReceiptClassification toDomain(
            ReceiptClassificationEntity entity
    ) {
        return ReceiptClassification.builder()
                .id(entity.getId())
                .receiptId(entity.getReceiptId())
                .merchant(entity.getMerchant())
                .amount(entity.getAmount())
                .category(entity.getCategory())
                .confidence(entity.getConfidence())
                .reason(entity.getReason())
                .classifiedAt(entity.getClassifiedAt())
                .build();
    }
}
