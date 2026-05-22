package com.jeronimo.validation_service.domain.repository;

import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import org.springframework.stereotype.Component;

@Component
public interface ReceiptValidationRepository {
    ReceiptValidation save(ReceiptValidation validation);
}
