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

        ReceiptValidation validation =
                ReceiptValidation.validate(
                        event.receiptId(),
                        event.merchant(),
                        event.amount()
                );

        ReceiptValidation savedValidation = repository.save(validation);

        log.info(
                "Receipt validated successfully. receiptId={}, status={}, reason={}",
                savedValidation.getReceiptId(),
                savedValidation.getStatus(),
                savedValidation.getReason()
        );

        eventPublisher.publishValidated(
                ValidationEventMapper.toValidatedEvent(savedValidation)
        );
    }
}
