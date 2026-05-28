package com.jeronimo.ai_classification_service.domain.service;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;

import java.math.BigDecimal;
import java.util.UUID;

public interface ReceiptAiClassifier {
    ReceiptClassification classify(
            UUID receiptId,
            String merchant,
            BigDecimal amount
    );
}
