package com.jeronimo.validation_service.application.usecase;

import com.jeronimo.validation_service.domain.event.ReceiptCreatedEvent;
import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.domain.repository.ReceiptValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateReceiptUseCase {

    private final ReceiptValidationRepository repository;

    public void execute(ReceiptCreatedEvent event) {

        ReceiptValidation validation =
                ReceiptValidation.validate(
                        event.receiptId(),
                        event.merchant(),
                        event.amount()
                );

        repository.save(validation);

        log.info(
                "Receipt validated successfully. receiptId={}, status={}",
                validation.getReceiptId(),
                validation.getStatus()
        );
    }
}
