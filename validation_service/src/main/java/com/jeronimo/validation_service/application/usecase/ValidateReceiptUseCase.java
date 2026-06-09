package com.jeronimo.validation_service.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeronimo.validation_service.application.mapper.ReceiptValidatedEventMapper;
import com.jeronimo.validation_service.domain.event.ReceiptCreatedEvent;
import com.jeronimo.validation_service.domain.event.ReceiptValidatedEvent;
import com.jeronimo.validation_service.domain.event.outbox.OutboxEvent;
import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.domain.repository.ReceiptValidationRepository;
import com.jeronimo.validation_service.domain.repository.outbox.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateReceiptUseCase {

    private final ReceiptValidationRepository repository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topics.receipt-validated}")
    private String receiptValidatedTopic;

    @Transactional
    public void execute(ReceiptCreatedEvent event) {
        if (repository.existsByReceiptId(event.receiptId())) {
            MDC.put("event", "receipt_validation_skipped");
            log.info("Receipt already validated. Skipping duplicated event");
            MDC.remove("event");
            return;
        }

        ReceiptValidation validation = ReceiptValidation.validate(
                event.receiptId(),
                event.merchant(),
                event.amount()
        );

        ReceiptValidation saved = repository.save(validation);

        ReceiptValidatedEvent validatedEvent =
                ReceiptValidatedEventMapper.toEvent(
                        saved,
                        event.correlationId()
                );

        String payload = null;
        try {
            payload = objectMapper.writeValueAsString(validatedEvent);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize receipt validated event", ex);
        }

        OutboxEvent outboxEvent = OutboxEvent.create(
                saved.getReceiptId(),
                "RECEIPT_VALIDATION",
                "RECEIPT_VALIDATED",
                receiptValidatedTopic,
                payload
        );

        outboxRepository.save(outboxEvent);

        MDC.put("event", "receipt_validation_outbox_saved");
        log.info("Receipt validation saved with outbox event");
        MDC.remove("event");
    }
}
