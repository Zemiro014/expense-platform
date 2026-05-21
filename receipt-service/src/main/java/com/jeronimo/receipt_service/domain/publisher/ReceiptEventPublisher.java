package com.jeronimo.receipt_service.domain.publisher;

import com.jeronimo.receipt_service.domain.event.ReceiptCreatedEvent;
import reactor.core.publisher.Mono;


public interface ReceiptEventPublisher {
    Mono<Void> publishReceiptCreated(ReceiptCreatedEvent receipt);
}
