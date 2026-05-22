package com.jeronimo.validation_service.infrastructure.persistence.repository;

import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataReceiptValidationRepository extends JpaRepository<ReceiptValidationEntity, UUID> {
}
