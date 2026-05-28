package com.jeronimo.ai_classification_service.infrastructure.kafka.consumer;

import com.jeronimo.ai_classification_service.application.usecase.ClassifyReceiptUseCase;
import com.jeronimo.ai_classification_service.domain.event.ReceiptCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiptCreatedConsumer {

    private final ClassifyReceiptUseCase classifyReceiptUseCase;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000),
            dltTopicSuffix = "-dlt",
            kafkaTemplate = "retryKafkaTemplate"
    )
    @KafkaListener(
            topics = "${app.kafka.topics.receipt-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ReceiptCreatedEvent event) {
        try {
            MDC.put("correlationId", event.correlationId());
            MDC.put("receiptId", event.receiptId().toString());
            MDC.put("flow", "receipt-ai-classification");
            MDC.put("event", "receipt_created_consumed");

            log.info("Receipt created event received for AI classification");

            classifyReceiptUseCase.execute(event);

        } finally {
            MDC.clear();
        }
    }

    @DltHandler
    public void handleDlt(
            ReceiptCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ) {
        try {
            MDC.put("correlationId", event.correlationId());
            MDC.put("receiptId", event.receiptId().toString());
            MDC.put("event", "receipt_created_ai_classification_sent_to_dlt");
            MDC.put("topic", topic);
            MDC.put("offset", String.valueOf(offset));
            MDC.put("partition", String.valueOf(partition));

            log.error("Receipt created event sent to AI classification DLT");

        } finally {
            MDC.clear();
        }
    }
}
