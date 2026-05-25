package com.jeronimo.validation_service.infrastructure.kafka.consumer;

import com.jeronimo.validation_service.application.usecase.ValidateReceiptUseCase;
import com.jeronimo.validation_service.domain.event.ReceiptCreatedEvent;
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

    private final ValidateReceiptUseCase validateReceiptUseCase;

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
        MDC.put("correlationId", event.correlationId());
        log.info("Receipt created event received. receiptId={}, correlationId={}",
                event.receiptId(),
                event.correlationId()
        );
        validateReceiptUseCase.execute(event);
        MDC.clear();
    }

    @DltHandler
    public void handlerDlt(
            ReceiptCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ){
        try {
            MDC.put("correlationId", event.correlationId());
            MDC.put("receiptId", event.receiptId().toString());
            MDC.put("event", "receipt_created_sent_to_dlt");
            MDC.put("topic", topic);
            MDC.put("offset", String.valueOf(offset));
            MDC.put("partition", String.valueOf(partition));

            log.error(
                    "Receipt created event sent to DLT. receiptId={}",
                    event.receiptId()
            );
        } finally {
            MDC.clear();
        }
    }
}
