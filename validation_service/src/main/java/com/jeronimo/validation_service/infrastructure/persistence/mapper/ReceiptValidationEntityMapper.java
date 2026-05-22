package com.jeronimo.validation_service.infrastructure.persistence.mapper;

import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;

public class ReceiptValidationEntityMapper {
    private ReceiptValidationEntityMapper() {
    }

    public static ReceiptValidationEntity toEntity(ReceiptValidation validation) {
        return ReceiptValidationEntity.builder()
                .id(validation.getId())
                .receiptId(validation.getReceiptId())
                .merchant(validation.getMerchant())
                .amount(validation.getAmount())
                .status(validation.getStatus())
                .reason(validation.getReason())
                .validatedAt(validation.getValidatedAt())
                .build();
    }

    public static ReceiptValidation toDomain(ReceiptValidationEntity entity) {
        return ReceiptValidation.restore(
                entity.getId(),
                entity.getReceiptId(),
                entity.getMerchant(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getReason(),
                entity.getValidatedAt()
        );
    }
}
