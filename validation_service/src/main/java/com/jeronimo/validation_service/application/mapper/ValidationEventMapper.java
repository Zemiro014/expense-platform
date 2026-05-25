package com.jeronimo.validation_service.application.mapper;

import com.jeronimo.validation_service.domain.event.ReceiptValidatedEvent;
import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import org.slf4j.MDC;

public class ValidationEventMapper {

    private ValidationEventMapper(){}

    public static ReceiptValidatedEvent toValidatedEvent(ReceiptValidation validation){
        return new ReceiptValidatedEvent(
                validation.getReceiptId(),
                validation.getStatus().toString(),
                validation.getReason(),
                validation.getValidatedAt(),
                MDC.get("correlationId")
        );
    }
}
