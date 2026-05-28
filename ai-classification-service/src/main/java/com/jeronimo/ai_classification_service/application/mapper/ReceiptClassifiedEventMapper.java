package com.jeronimo.ai_classification_service.application.mapper;

import com.jeronimo.ai_classification_service.domain.event.ReceiptClassifiedEvent;
import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;

public class ReceiptClassifiedEventMapper {
    private ReceiptClassifiedEventMapper() {
    }

    public static ReceiptClassifiedEvent toEvent(
            ReceiptClassification classification,
            String correlationId
    ) {
        return new ReceiptClassifiedEvent(
                classification.getReceiptId(),
                classification.getCategory().name(),
                classification.getConfidence(),
                classification.getReason(),
                classification.getClassifiedAt(),
                correlationId
        );
    }
}
