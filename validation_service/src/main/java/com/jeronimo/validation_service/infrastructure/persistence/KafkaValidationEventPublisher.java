package com.jeronimo.validation_service.infrastructure.persistence;

import com.jeronimo.validation_service.domain.event.ReceiptValidatedEvent;
import com.jeronimo.validation_service.domain.publisher.ValidationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaValidationEventPublisher implements ValidationEventPublisher {

    private final KafkaTemplate<String, ReceiptValidatedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.receipt-validated}")
    private String receiptValidatedTopic;

    @Override
    public void publishValidated(ReceiptValidatedEvent event) {
        kafkaTemplate.send(
                receiptValidatedTopic,
                event.receiptId().toString(),
                event
        );

        log.info(
                "Receipt validated event published. receiptId={}, status={}",
                event.receiptId(),
                event.status()
        );
    }
}
