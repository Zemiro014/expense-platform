package com.jeronimo.validation_service.application.mapper;

import com.jeronimo.validation_service.domain.event.ReceiptValidatedEvent;
import com.jeronimo.validation_service.domain.model.ReceiptValidation;

public class ReceiptValidatedEventMapper {

    public static ReceiptValidatedEvent toEvent(
            ReceiptValidation validation,
            String correlationId
    ) {
        return new ReceiptValidatedEvent(
                validation.getReceiptId(),
                validation.getStatus().toString(),
                validation.getReason(),
                validation.getValidatedAt(),
                correlationId
        );
    }
}
