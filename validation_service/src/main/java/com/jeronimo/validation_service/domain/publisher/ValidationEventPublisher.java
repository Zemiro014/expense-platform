package com.jeronimo.validation_service.domain.publisher;

import com.jeronimo.validation_service.domain.event.ReceiptValidatedEvent;

public interface ValidationEventPublisher {
    void publishValidated(ReceiptValidatedEvent event);
}
