package com.jeronimo.validation_service.application.usecase;

import com.jeronimo.validation_service.application.mapper.ValidationEventMapper;
import com.jeronimo.validation_service.domain.event.ReceiptCreatedEvent;
import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.domain.publisher.ValidationEventPublisher;
import com.jeronimo.validation_service.domain.repository.ReceiptValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateReceiptUseCase {

    private final ReceiptValidationRepository repository;
    private final ValidationEventPublisher eventPublisher;

    public void execute(ReceiptCreatedEvent event) {
        if (repository.existsByReceiptId(event.receiptId())) {
            log.info(
                    "Receipt already validated. Skipping duplicated event. receiptId={}, correlationId={}",
                    event.receiptId(),
                    event.correlationId()
            );
            return;
        }

        ReceiptValidation validation =
                ReceiptValidation.validate(
                        event.receiptId(),
                        event.merchant(),
                        event.amount()
                );

        ReceiptValidation savedValidation = repository.save(validation);

        log.info(
                "Receipt validated and saved successfully. receiptId={}, status={}, reason={}, correlationId={}",
                savedValidation.getReceiptId(),
                savedValidation.getStatus(),
                savedValidation.getReason(),
                event.correlationId()
        );

        eventPublisher.publishValidated(
                ValidationEventMapper.toValidatedEvent(savedValidation)
        );
    }
}
