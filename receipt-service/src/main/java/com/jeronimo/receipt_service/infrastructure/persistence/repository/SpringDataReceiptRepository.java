package com.jeronimo.receipt_service.infrastructure.persistence.repository;

import com.jeronimo.receipt_service.infrastructure.persistence.entity.ReceiptEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface SpringDataReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, UUID> {
}
