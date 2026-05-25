package com.jeronimo.validation_service.domain.repository;

import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;

import java.util.List;

public interface ReceiptValidationRepository {
    ReceiptValidation save(ReceiptValidation validation);

    List<ReceiptValidationEntity> findAll();
}
