package com.jeronimo.ai_classification_service.infrastructure.kafka.publisher;

import com.jeronimo.ai_classification_service.domain.event.ReceiptClassifiedEvent;
import com.jeronimo.ai_classification_service.domain.publisher.ReceiptClassificationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReceiptClassificationEventPublisher
        implements ReceiptClassificationEventPublisher {

    private final KafkaTemplate<String, ReceiptClassifiedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.receipt-classified}")
    private String receiptClassifiedTopic;

    @Override
    public void publishClassified(ReceiptClassifiedEvent event) {
        kafkaTemplate.send(
                receiptClassifiedTopic,
                event.receiptId().toString(),
                event
        );

        MDC.put("event", "receipt_classified_published");
        MDC.put("topic", receiptClassifiedTopic);

        log.info("Receipt classified event published successfully");

        MDC.remove("event");
        MDC.remove("topic");
    }
}
