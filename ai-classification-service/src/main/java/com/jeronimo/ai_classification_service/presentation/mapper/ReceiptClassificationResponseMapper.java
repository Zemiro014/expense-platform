package com.jeronimo.ai_classification_service.presentation.mapper;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.presentation.response.ReceiptClassificationResponse;

public class ReceiptClassificationResponseMapper {

    private ReceiptClassificationResponseMapper() {
    }

    public static ReceiptClassificationResponse toResponse(
            ReceiptClassification classification
    ) {
        return new ReceiptClassificationResponse(
                classification.getId(),
                classification.getReceiptId(),
                classification.getMerchant(),
                classification.getAmount(),
                classification.getCategory().name(),
                classification.getConfidence(),
                classification.getReason(),
                classification.getClassifiedAt()
        );
    }
}
