package com.jeronimo.validation_service.infrastructure.kafka.consumer;

import com.jeronimo.validation_service.application.usecase.ValidateReceiptUseCase;
import com.jeronimo.validation_service.domain.event.ReceiptCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
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
        log.info("Receipt created event received. receiptId={}", event.receiptId());
        validateReceiptUseCase.execute(event);
    }

    @DltHandler
    public void handlerDlt(ReceiptCreatedEvent event){
        log.error(
            "Receipt created event sent to DLT. receiptId={}",
            event.receiptId()
        );
    }
}
