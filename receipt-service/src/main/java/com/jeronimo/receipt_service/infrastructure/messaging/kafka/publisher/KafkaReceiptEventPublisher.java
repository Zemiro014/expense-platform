package com.jeronimo.receipt_service.infrastructure.messaging.kafka.publisher;

import com.jeronimo.receipt_service.domain.event.ReceiptCreatedEvent;
import com.jeronimo.receipt_service.domain.publisher.ReceiptEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaReceiptEventPublisher implements ReceiptEventPublisher {
    private final KafkaTemplate<String, ReceiptCreatedEvent> eventKafkaTemplate;

    @Value("${app.kafka.topics.receipt-created}")
    private String receiptCreatedTopic;

    @Override
    public Mono<Void> publishReceiptCreated(ReceiptCreatedEvent receiptCreatedEvent) {
        if (receiptCreatedTopic == null || receiptCreatedTopic.isBlank()){
            throw new IllegalArgumentException("topic is required");
        }
        if(receiptCreatedEvent.receiptId() == null){
            throw new IllegalArgumentException("receiptId is required");
        }
        return Mono.fromFuture(() ->
                eventKafkaTemplate.send(
                        receiptCreatedTopic,
                        receiptCreatedEvent.receiptId().toString(),
                        receiptCreatedEvent
                )).then();
    }
}
