package com.jeronimo.ai_classification_service.domain.publisher;

import com.jeronimo.ai_classification_service.domain.event.ReceiptClassifiedEvent;

public interface ReceiptClassificationEventPublisher {
    void publishClassified(ReceiptClassifiedEvent event);
}
