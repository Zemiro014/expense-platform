package com.jeronimo.validation_service.application.mapper;

import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.presentation.response.ReceiptValidationResponse;

public class ReceiptValidationMapper {

    private ReceiptValidationMapper(){}

    public static ReceiptValidationResponse toResponse(ReceiptValidation entity){
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
