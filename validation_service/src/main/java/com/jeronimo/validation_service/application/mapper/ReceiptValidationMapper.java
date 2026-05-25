package com.jeronimo.validation_service.application.mapper;

import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;
import com.jeronimo.validation_service.presentation.response.ReceiptValidationResponse;

public class ReceiptValidationMapper {

    private ReceiptValidationMapper(){}

    public static ReceiptValidationResponse toResponse(ReceiptValidationEntity entity){
        return new ReceiptValidationResponse(
                entity.getId(),
                entity.getReceiptId(),
                entity.getMerchant(),
                entity.getAmount(),
                entity.getStatus().toString(),
                entity.getReason(),
                entity.getValidatedAt()
        );
    }

}
