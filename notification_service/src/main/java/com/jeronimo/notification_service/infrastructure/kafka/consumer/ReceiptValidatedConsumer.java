package com.jeronimo.notification_service.infrastructure.kafka.consumer;
import com.jeronimo.notification_service.application.usecase.CreateNotificationUseCase;
import com.jeronimo.notification_service.domain.event.ReceiptValidatedEvent;
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
public class ReceiptValidatedConsumer {

    private final CreateNotificationUseCase createNotificationUseCase;

    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 2000),
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
        topics = "${app.kafka.topics.receipt-validated}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ReceiptValidatedEvent event) {

        try {
            MDC.put("correlationId", event.correlationId());
            MDC.put("receiptId", event.receiptId().toString());
            MDC.put("event", "receipt_validated_consumed");
            log.info("Receipt validated event received");

            createNotificationUseCase.execute(event);

        } finally {MDC.clear();}
    }

    @DltHandler
    public void handleDlt(
            ReceiptValidatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC)
            String topic,

            @Header(KafkaHeaders.OFFSET)
            long offset
    ) {
        try {
            MDC.put("correlationId", event.correlationId());
            MDC.put("receiptId", event.receiptId().toString());
            MDC.put("event", "receipt_validated_sent_to_dlt");
            MDC.put("topic", topic);
            MDC.put("offset", String.valueOf(offset));

            log.error("Receipt validated event sent to DLT");

        } finally {MDC.clear();}
    }
}
