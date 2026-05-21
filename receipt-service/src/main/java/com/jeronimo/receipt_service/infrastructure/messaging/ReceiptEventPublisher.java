package com.jeronimo.receipt_service.infrastructure.messaging;

import com.jeronimo.receipt_service.domain.model.Receipt;
import org.springframework.stereotype.Component;

@Component
public class ReceiptEventPublisher {
    public void publishReceiptCreated(Receipt receipt) {
    }
}
