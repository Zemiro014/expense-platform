package com.jeronimo.validation_service.infrastructure.kafka.consumer;

import com.jeronimo.validation_service.application.usecase.ValidateReceiptUseCase;
import com.jeronimo.validation_service.domain.event.ReceiptCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiptCreatedConsumer {

    private final ValidateReceiptUseCase validateReceiptUseCase;

    @KafkaListener(
            topics = "${app.kafka.topics.receipt-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ReceiptCreatedEvent event) {
        log.info("Receipt created event received. receiptId={}", event.receiptId());
        validateReceiptUseCase.execute(event);
    }
}
